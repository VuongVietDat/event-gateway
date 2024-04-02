package vn.com.atomi.loyalty.eventgateway.service.impl;

import java.io.IOException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.ThreadContext;
import org.dhatim.fastexcel.reader.Cell;
import org.dhatim.fastexcel.reader.ReadableWorkbook;
import org.dhatim.fastexcel.reader.Row;
import org.dhatim.fastexcel.reader.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vn.com.atomi.loyalty.base.data.BaseService;
import vn.com.atomi.loyalty.base.data.ResponsePage;
import vn.com.atomi.loyalty.base.exception.BaseException;
import vn.com.atomi.loyalty.eventgateway.dto.input.CardTransactionInfoInput;
import vn.com.atomi.loyalty.eventgateway.dto.output.CardTransactionFileOutput;
import vn.com.atomi.loyalty.eventgateway.dto.output.CardTransactionInfoOutput;
import vn.com.atomi.loyalty.eventgateway.entity.CardTransactionFile;
import vn.com.atomi.loyalty.eventgateway.entity.CardTransactionInfo;
import vn.com.atomi.loyalty.eventgateway.enums.ErrorCode;
import vn.com.atomi.loyalty.eventgateway.enums.StatusCardTransaction;
import vn.com.atomi.loyalty.eventgateway.repository.redis.CardTransactionFileRepository;
import vn.com.atomi.loyalty.eventgateway.repository.redis.CardTransactionInfoRepository;
import vn.com.atomi.loyalty.eventgateway.repository.redis.CustomRepository;
import vn.com.atomi.loyalty.eventgateway.service.CardTransactionService;

@Slf4j
@Service
@RequiredArgsConstructor
public class CardTransactionServiceImpl extends BaseService implements CardTransactionService {

  @Autowired private CardTransactionInfoRepository cardTransactionInfoRepository;

  @Autowired private CardTransactionFileRepository cardTransactionFileRepository;

  private final ApplicationEventPublisher applicationEventPublisher;
  private final CustomRepository customRepository;
  private final ThreadPoolTaskExecutor threadPoolTaskExecutor;

  /**
   * Chia 2 luồng xử lý bất đồng bộ
   *
   * @param transactionFile
   */
  @Override
  public void uploadTransactionFile(MultipartFile transactionFile) {
    /*Luồng 1 :  Khởi tạo bản ghi file transaction */
    String fileName = transactionFile.getOriginalFilename();
    log.info("uploadTransactionFile {}", fileName);
    CardTransactionFile cardTransactionFile =
        createCardTransactionFile(fileName, StatusCardTransaction.INITIALIZING);
    cardTransactionFile = cardTransactionFileRepository.save(cardTransactionFile);

    var context = ThreadContext.getContext();
    CardTransactionFile finalCardTransactionFile = cardTransactionFile;
    /*Luồng 2 : Đọc & Lưu thông tin chi tiết file transaction */
    threadPoolTaskExecutor.execute(
        () -> {
          ThreadContext.putAll(context);
          try {
            if (isCheckExcelFile(transactionFile)) {
              finalCardTransactionFile.setStatusCard(StatusCardTransaction.INITIALIZE_ERROR);
              cardTransactionFileRepository.save(finalCardTransactionFile);
            }
            /* Giới hạn từng lô bản ghi là 100 */
            int batchSize = 100;
            ReadableWorkbook workbook = null;
            workbook = new ReadableWorkbook(transactionFile.getInputStream());
            Sheet sheet = workbook.getFirstSheet();
            var rows = sheet.read();
            var mapIndex = getTitleIndex(rows.get(0));
            var n = rows.size();
            final int[] totalSuccessful = {0};
            final int[] totalFailed = {0};
            BigDecimal totalTransactionMoney = BigDecimal.ZERO;
            List<CardTransactionInfo> card = new ArrayList<>();
            for (int i = 1; i < n; i++) {
              card.add(createEntitiesFromRows(mapIndex, rows.get(i)));
              try {
                if (card.size() == batchSize || i == n - 1) {
                  customRepository.saveAllCardTransactionInfos(
                      card, finalCardTransactionFile.getId());
                  totalSuccessful[0] += card.size();
                  totalTransactionMoney =
                      card.stream()
                          .map(item -> new BigDecimal(item.getTotalAmount()))
                          .reduce(BigDecimal.ZERO, BigDecimal::add);
                  card.clear();
                }
              } catch (BaseException e) {
                totalFailed[0] += card.size();
                LOGGER.error(
                    "Error saving card: " + totalFailed[0] + ", Exception: " + e.getMessage());
                card.clear();
              }
            }
            finalCardTransactionFile.setTotalRecordSuccessful(String.valueOf(totalSuccessful[0]));
            finalCardTransactionFile.setTotalRecordFailed(String.valueOf(totalFailed[0]));
            finalCardTransactionFile.setTotalTransactionMoney(
                String.valueOf(totalTransactionMoney));
            finalCardTransactionFile.setStatusCard(StatusCardTransaction.IN_PROGRESS);
            cardTransactionFileRepository.save(finalCardTransactionFile);
            LOGGER.info("Success");
          } catch (BaseException e) {
            finalCardTransactionFile.setStatusCard(StatusCardTransaction.INITIALIZE_ERROR);
            cardTransactionFileRepository.save(finalCardTransactionFile);
          } catch (IOException e) {
            LOGGER.error(String.valueOf(e));
          }
        });
  }

  public static boolean isCheckExcelFile(MultipartFile file) {
    String fileName = file.getName();
    if (!fileName.endsWith(".xls") && !fileName.endsWith(".xlsx")) {
      return false;
    }
    return true;
  }

  private Map<Integer, String> getTitleIndex(Row row0) {
    Map<String, String> columnIndexToFieldName = new HashMap<>();
    columnIndexToFieldName.put("CIF", "cif");
    columnIndexToFieldName.put("TEN_KH", "customerName");
    columnIndexToFieldName.put("CARD_ID", "cardId");
    columnIndexToFieldName.put("SO_THE", "cardNumber");
    columnIndexToFieldName.put("MA_SAN_PHAM", "productId");
    columnIndexToFieldName.put("HANG_THE", "cardRank");
    columnIndexToFieldName.put("CARD_CATEGORY", "cardCategory");
    columnIndexToFieldName.put("HAN_MUC_THE", "cardLimit");
    columnIndexToFieldName.put("DVKD_PHAT_HANH", "issueOrganization");
    columnIndexToFieldName.put("SO_DIEN_THOAI", "phoneNumber");
    columnIndexToFieldName.put("SO_LUONG_GD", "totalTransaction");
    columnIndexToFieldName.put("TONG_SO_TIEN_GD", "totalAmount");
    columnIndexToFieldName.put("NGHI NGỜ ĐÁO HẠN", "maturityDoubt");

    Map<Integer, String> indexMap = new HashMap<>();
    row0.forEach(
        cell -> {
          var fieldName = columnIndexToFieldName.get(cell.getRawValue());
          if (fieldName != null) {
            indexMap.put(cell.getColumnIndex(), fieldName);
          }
        });

    return indexMap;
  }

  @Override
  public void updateCardTransaction(CardTransactionInfoInput cardTransactionInfoInput) {
    var cardTransactionInfo =
        cardTransactionInfoRepository
            .findById(cardTransactionInfoInput.getId())
            .orElseThrow(() -> new BaseException(ErrorCode.RECORD_NOT_EXISTED));

    super.modelMapper.updateCardTransactionInfo(cardTransactionInfoInput, cardTransactionInfo);
    cardTransactionInfoRepository.save(cardTransactionInfo);
  }

  @Override
  public CardTransactionFileOutput getDetailCardTransaction(Long id) {
    var cardTransactionFile =
        cardTransactionFileRepository
            .findByDeletedFalseAndId(id)
            .orElseThrow(() -> new BaseException(ErrorCode.RECORD_NOT_EXISTED));
    return super.modelMapper.getDetail(cardTransactionFile);
  }

  @Override
  public ResponsePage<CardTransactionInfoOutput> getDetailCardTransactionInfo(
      Long id, Pageable pageable) {
    var cardTransactionInfo = cardTransactionInfoRepository.findByCondition(id, pageable);
    return new ResponsePage<>(
        cardTransactionInfo,
        super.modelMapper.getDetailCardTransactionInfo(cardTransactionInfo.getContent()));
  }

  @Override
  public ResponsePage<CardTransactionFileOutput> getListTransactionFile(
      Long id,
      Date startTransactionDate,
      Date endTransactionDate,
      String statusCard,
      String createdBy,
      Pageable pageable) {
    var page =
        cardTransactionFileRepository.getListCardTransactionFile(
            id, startTransactionDate, endTransactionDate, statusCard, createdBy, pageable);
    return new ResponsePage<>(
        page, super.modelMapper.convertToCardTransactionInfoOutPut(page.getContent()));
  }

  private CardTransactionInfo createEntitiesFromRows(Map<Integer, String> mapIndex, Row row) {
    CardTransactionInfo cardTransactionInfo = new CardTransactionInfo();

    for (Cell cell : row) {
      var fieldName = mapIndex.get(cell.getColumnIndex());
      if (fieldName == null) {
        continue;
      }
      var value = cell.getText().trim();
      Field field;
      try {
        field = CardTransactionInfo.class.getDeclaredField(fieldName);
        field.setAccessible(true);

        if (field.getType().equals(String.class)) {
          field.set(cardTransactionInfo, value);
        } else if (field.getType().equals(int.class) || field.getType().equals(Integer.class)) {
          field.set(cardTransactionInfo, Integer.parseInt(value));
        }
      } catch (NoSuchFieldException | IllegalAccessException e) {
        LOGGER.error(String.valueOf(e));
      }
    }

    return cardTransactionInfo;
  }

  private CardTransactionFile createCardTransactionFile(
      String fileName, StatusCardTransaction statusCard) {
    CardTransactionFile cardTransactionFile = new CardTransactionFile();
    cardTransactionFile.setName(fileName);
    cardTransactionFile.setStatusCard(statusCard);
    cardTransactionFileRepository.save(cardTransactionFile);
    return cardTransactionFile;
  }
}
