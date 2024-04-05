package vn.com.atomi.loyalty.eventgateway.service.impl;

import java.io.IOException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vn.com.atomi.loyalty.base.constant.RequestConstant;
import vn.com.atomi.loyalty.base.data.BaseService;
import vn.com.atomi.loyalty.base.data.ResponsePage;
import vn.com.atomi.loyalty.base.event.MessageData;
import vn.com.atomi.loyalty.base.event.MessageInterceptor;
import vn.com.atomi.loyalty.base.exception.BaseException;
import vn.com.atomi.loyalty.eventgateway.dto.input.CardTransactionInfoInput;
import vn.com.atomi.loyalty.eventgateway.dto.message.AllocationPointMessage;
import vn.com.atomi.loyalty.eventgateway.dto.message.AllocationPointTransactionInput;
import vn.com.atomi.loyalty.eventgateway.dto.output.*;
import vn.com.atomi.loyalty.eventgateway.entity.CardTransactionFile;
import vn.com.atomi.loyalty.eventgateway.entity.CardTransactionInfo;
import vn.com.atomi.loyalty.eventgateway.enums.*;
import vn.com.atomi.loyalty.eventgateway.feign.LoyaltyConfigClient;
import vn.com.atomi.loyalty.eventgateway.feign.LoyaltyCoreClient;
import vn.com.atomi.loyalty.eventgateway.repository.CardTransactionFileRepository;
import vn.com.atomi.loyalty.eventgateway.repository.CardTransactionInfoRepository;
import vn.com.atomi.loyalty.eventgateway.repository.CustomRepository;
import vn.com.atomi.loyalty.eventgateway.service.CardTransactionService;
import vn.com.atomi.loyalty.eventgateway.utils.Constants;
import vn.com.atomi.loyalty.eventgateway.utils.Utils;

@Slf4j
@Service
@RequiredArgsConstructor
public class CardTransactionServiceImpl extends BaseService implements CardTransactionService {

  private final CardTransactionInfoRepository cardTransactionInfoRepository;
  private final CardTransactionFileRepository cardTransactionFileRepository;
  private final CustomRepository customRepository;
  private final LoyaltyCoreClient loyaltyCoreClient;
  private final LoyaltyConfigClient loyaltyConfigClient;
  private final MessageInterceptor messageInterceptor;
  private final ThreadPoolTaskExecutor threadPoolTaskExecutor;

  @Value("${custom.properties.kafka.topic.allocation-point-event.name}")
  private String allocationPointTopic;

  public static boolean isCheckExcelFile(MultipartFile file) {
    String fileName = file.getName();
    return fileName.endsWith(".xls") || fileName.endsWith(".xlsx");
  }

  @Override
  public void uploadTransactionFile(MultipartFile transactionFile) {
    /*Luồng 1 :  Khởi tạo bản ghi file transaction */
    String fileName = transactionFile.getOriginalFilename();
    CardTransactionFile cardTransactionFile = new CardTransactionFile();
    cardTransactionFile.setName(fileName);
    cardTransactionFile.setStatusCard(StatusCardTransaction.INITIALIZING);
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
            finalCardTransactionFile.setTotalRecordSuccessful(totalSuccessful[0]);
            finalCardTransactionFile.setTotalRecordFailed(totalFailed[0]);
            finalCardTransactionFile.setTotalTransactionMoney(
                String.valueOf(totalTransactionMoney));
            finalCardTransactionFile.setStatusCard(StatusCardTransaction.IN_PROGRESS);
            cardTransactionFileRepository.save(finalCardTransactionFile);
            LOGGER.info("Success");
          } catch (BaseException e) {
            finalCardTransactionFile.setStatusCard(StatusCardTransaction.INITIALIZE_ERROR);
            cardTransactionFileRepository.save(finalCardTransactionFile);
          } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
          }
        });
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
            .findByDeletedFalseAndId(cardTransactionInfoInput.getId())
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
    var cardTransactionInfo =
        cardTransactionInfoRepository.findByDeletedFalseAndCardTransactionFileId(id, pageable);
    return new ResponsePage<>(
        cardTransactionInfo,
        super.modelMapper.getDetailCardTransactionInfo(cardTransactionInfo.getContent()));
  }

  @Override
  public ResponsePage<CardTransactionFileOutput> getListTransactionFile(
      Long id,
      String startTransactionDate,
      String endTransactionDate,
      StatusCardTransaction statusCard,
      String createdBy,
      String nameFile,
      Pageable pageable) {
    var page =
        cardTransactionFileRepository.getListCardTransactionFile(
            id,
            Utils.convertToLocalDateTimeStartDay(startTransactionDate),
            Utils.convertToLocalDateTimeStartDay(endTransactionDate),
            statusCard,
            createdBy,
            nameFile,
            pageable);
    return new ResponsePage<>(
        page, super.modelMapper.convertToCardTransactionInfoOutPut(page.getContent()));
  }

  @Override
  public void confirmCardTransactionFile(Long id, boolean accept) {
    cardTransactionFileRepository
        .findByDeletedFalseAndId(id)
        .ifPresentOrElse(
            cardTransactionFile -> {
              cardTransactionFile.setStatusCard(
                  accept ? StatusCardTransaction.IN_PROGRESS : StatusCardTransaction.REJECT);
              cardTransactionFileRepository.save(cardTransactionFile);
              if (accept) {
                var sourceMaps =
                    loyaltyConfigClient
                        .getAllSourceDataMap(
                            ThreadContext.get(RequestConstant.REQUEST_ID), SourceGroup.CARD)
                        .getData();
                var productLines =
                    loyaltyConfigClient
                        .getProductLines(ThreadContext.get(RequestConstant.REQUEST_ID))
                        .getData();
                var context = ThreadContext.getContext();
                threadPoolTaskExecutor.execute(
                    () -> {
                      ThreadContext.putAll(context);
                      var totalPage = cardTransactionFile.getTotalRecordSuccessful() / 1000 + 1;
                      for (int i = 0; i < totalPage; i++) {
                        var page =
                            cardTransactionInfoRepository
                                .findByDeletedFalseAndCardTransactionFileId(
                                    id, PageRequest.of(i, 1000));
                        if (!page.getContent().isEmpty()) {
                          for (CardTransactionInfo info : page) {
                            var sourceMap =
                                sourceMaps.stream()
                                    .filter(
                                        v ->
                                            v.getSourceType()
                                                    .equals(Constants.SourceType.PRODUCT_LINE)
                                                && v.getSourceId().equals(info.getProductId()))
                                    .findFirst()
                                    .orElse(new SourceDataMapOutput());
                            CustomerOutput customerOutput =
                                loyaltyCoreClient
                                    .getCustomer(
                                        ThreadContext.get(RequestConstant.REQUEST_ID),
                                        null,
                                        info.getCif())
                                    .getData();
                            AllocationPointMessage allocationPointMessage =
                                AllocationPointMessage.builder()
                                    .transaction(
                                        AllocationPointTransactionInput.builder()
                                            .transactionAt(LocalDateTime.now())
                                            .productLine(sourceMap.getDestinationCode())
                                            .productType(
                                                productLines.stream()
                                                    .filter(
                                                        v ->
                                                            v.getLineCode()
                                                                .equals(
                                                                    sourceMap.getDestinationCode()))
                                                    .findFirst()
                                                    .orElse(new ProductLineOutput())
                                                    .getProductType())
                                            .refNo(info.getRefNo())
                                            .amount(Long.parseLong(info.getTotalAmount()))
                                            .build())
                                    .type(RuleType.TRANSACTION)
                                    .pointEventSource(PointEventSource.UPLOAD_CARD_TRANSACTION)
                                    .customer(customerOutput)
                                    .build();
                            messageInterceptor.convertAndSend(
                                allocationPointTopic,
                                customerOutput.getId().toString(),
                                new MessageData<>(allocationPointMessage));
                          }
                        }
                      }
                      cardTransactionFile.setStatusCard(StatusCardTransaction.COMPLETE);
                      ThreadContext.clearAll();
                    });
              }
            },
            () -> {
              LOGGER.error("Not found card upload file");
            });
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
        LOGGER.error(e.getMessage(), e);
      }
    }

    return cardTransactionInfo;
  }
}
