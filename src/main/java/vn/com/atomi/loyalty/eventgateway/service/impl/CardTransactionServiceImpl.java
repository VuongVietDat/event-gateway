package vn.com.atomi.loyalty.eventgateway.service.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.dhatim.fastexcel.reader.Cell;
import org.dhatim.fastexcel.reader.ReadableWorkbook;
import org.dhatim.fastexcel.reader.Row;
import org.dhatim.fastexcel.reader.Sheet;
import org.modelmapper.ModelMapper;
import org.modelmapper.internal.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
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

  @Autowired
  private CardTransactionInfoRepository cardTransactionInfoRepository;

  @Autowired
  private CardTransactionFileRepository cardTransactionFileRepository;

  private final CustomRepository customRepository;
  private final ModelMapper modelMapper;

  @Transactional
  @Override
  public void uploadTransactionFile(MultipartFile transactionFile) {
    String fileName = transactionFile.getOriginalFilename();
    log.info("uploadTransactionFile {}", fileName);
    CardTransactionFile cardTransactionFile = createCardTransactionFile(fileName,
        StatusCardTransaction.INITIALIZING);
    cardTransactionFileRepository.save(cardTransactionFile);

    try {
      CompletableFuture.completedFuture(
              Pair.of(cardTransactionFile.getId(), transactionFile.getInputStream()))
          .thenAcceptAsync(pair -> {
            log.info("thenAcceptAsync ");
            try {
              List<CardTransactionInfo> batch = new ArrayList<>();
              int batchSize = 100;
              int count = 0;
              ReadableWorkbook workbook = null;
              try {
                workbook = new ReadableWorkbook(pair.getRight());
              } catch (IOException e) {
                throw new RuntimeException(e);
              }
              Sheet sheet = workbook.getFirstSheet();
              Iterator<Row> rowIterator = null;
              var rows = sheet.read();
              var mapIndex = getTitleIndex(rows.get(0));
              var n = rows.size();
              List<CardTransactionInfo> card = new ArrayList<>();
              for (int i = 1; i < n; i++) {
                card.add(createEntitiesFromRows(mapIndex,
                    rows.get(i)));
              }
              System.out.println("card " + card.size());
              //saves
              customRepository.saveAllCardTransactionInfos(card);

              cardTransactionFile.setStatusCard(StatusCardTransaction.COMPLETE);
              cardTransactionFileRepository.save(cardTransactionFile);
              System.out.println("Complete");
              //
              //            try {
              //              rowIterator = sheet.read().iterator();
              //            } catch (IOException e) {
              //              throw new RuntimeException(e);
              //            }
              //            boolean isFirstRow = true;
              //            Row titleRow = sheet.read().get(0);
              //            while (rowIterator.hasNext()) {
              //              Row row = rowIterator.next();
              //              if (isFirstRow) {
              //                isFirstRow = false;
              //                continue;
              //              }
              //              if (row != null) {
              //                boolean isRowEmpty = true;
              //                Iterator<Cell> cellIterator = row.iterator();
              //                int columnCount = 0;
              //                while (cellIterator.hasNext() && columnCount < 13) {
              //                  Cell cell = cellIterator.next();
              //                  if (cell != null && cell.getText() != null && !cell.getText().isEmpty()) {
              //                    isRowEmpty = false;
              //                  }
              //                  columnCount++;
              //                }
              //                if (!isRowEmpty) {
              //                  CardTransactionInfo cardTransactionInfo = createEntityFromRow(row);
              //                  if (cardTransactionInfo != null) {
              //                    cardTransactionInfo.setCardTransactionFileId(pair.getLeft());
              //                    batch.add(cardTransactionInfo);
              //                    count++;
              //                    if (count % batchSize == 0) {
              //                      cardTransactionInfoRepository.saveAll(batch);
              //                      cardTransactionInfoRepository.save(cardTransactionInfo);
              //                      batch.clear();
              //                    }
              //                  }
              //                }
              //              }
              //            }
              //            if (!batch.isEmpty()) {
              //              cardTransactionInfoRepository.saveAll(batch);
              //              cardTransactionFile.setStatusCard(StatusCardTransaction.COMPLETE);
              //              cardTransactionFileRepository.save(cardTransactionFile);
              //              System.out.println("Complete");
              //            }
            } catch (BaseException e) {
              cardTransactionFile.setStatusCard(StatusCardTransaction.INITIALIZE_ERROR);
              cardTransactionFileRepository.save(cardTransactionFile);
            } catch (IOException e) {
              throw new RuntimeException(e);
            }
            log.info("complete");
          });
    } catch (IOException e) {
      log.info("IOException " + e);
    }
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
    columnIndexToFieldName.put("SO_DIEN_THOAI	SO_LUONG_GD", "phoneNumber");
    columnIndexToFieldName.put("SO_LUONG_GD", "totalTransaction");
    columnIndexToFieldName.put("TONG_SO_TIEN_GD", "totalAmount");
    columnIndexToFieldName.put("NGHI NGỜ ĐÁO HẠN", "maturityDoubt");

    Map<Integer, String> indexMap = new HashMap<>();
    row0.forEach(cell -> {
      var fieldName = columnIndexToFieldName.get(cell.getRawValue());
      if (fieldName != null) {
        indexMap.put(cell.getColumnIndex(), fieldName);
      }
    });

    return indexMap;
  }

  @Override
  public void updateCardTransaction(CardTransactionInfoInput cardTransactionInfoInput) {
    CardTransactionInfo cardTransactionInfo = cardTransactionInfoRepository
        .findById(cardTransactionInfoInput.getId())
        .orElseThrow(() -> new BaseException(ErrorCode.RECORD_NOT_EXISTED));

    modelMapper.getConfiguration().setSkipNullEnabled(true);
    modelMapper.map(cardTransactionInfoInput, cardTransactionInfo);
    cardTransactionInfoRepository.save(cardTransactionInfo);
  }

  @Override
  public CardTransactionFileOutput getDetailCardTransaction(Long id) {
    var cardTransactionFile =
        cardTransactionFileRepository.findByDeletedFalseAndId(id)
            .orElseThrow(() -> new BaseException(ErrorCode.RECORD_NOT_EXISTED));
    return super.modelMapper.getDetail(cardTransactionFile);
  }

  @Override
  public ResponsePage<CardTransactionInfoOutput> getDetailCardTransactionInfo(Long id,
      Pageable pageable) {
    var cardTransactionInfo =
        cardTransactionInfoRepository.findByCondition(
            id,
            pageable
        );
    if (!CollectionUtils.isEmpty(cardTransactionInfo.getContent())) {
      return new ResponsePage<>(
          cardTransactionInfo,
          super.modelMapper.getDetailCardTransactionInfo(cardTransactionInfo.getContent()));
    }
    return null;
  }

  private CardTransactionInfo createEntitiesFromRows(Map<Integer, String> mapIndex, Row row) {
//    System.out.println("row " + row.getRowNum());

    CardTransactionInfo cardTransactionInfo = new CardTransactionInfo();

    for (Cell cell : row) {
      var fieldName = mapIndex.get(cell.getColumnIndex());
      if (fieldName == null) {
        continue;
      }
//      if (cell.getColumnIndex() == 12) {
//        System.out.println("cell " + cell.toString());
//      }
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
        e.printStackTrace();
      }
//      System.out.println("cell " + cell.getColumnIndex());
    }

    return cardTransactionInfo;
  }

//  private CardTransactionInfo createEntityFromRow(Map<Integer, String> mapIndex, Row row)
//      throws IllegalAccessException {
//    CardTransactionInfo cardTransactionInfo = new CardTransactionInfo();
//
//    for (Cell cell : row) {
//      var fieldName = mapIndex.get(cell.getColumnIndex());
//      if (fieldName == null) {
//        continue;
//      }
//
//      var value = cell.getRawValue().trim();
//      Field field;
//      try {
//        field = CardTransactionInfo.class.getDeclaredField(fieldName);
//        field.setAccessible(true);
//
//        if (field.getType().equals(String.class)) {
//          field.set(cardTransactionInfo, value);
//        } else if (field.getType().equals(int.class) || field.getType().equals(Integer.class)) {
//          field.set(cardTransactionInfo, value);
//        }
//      } catch (NoSuchFieldException | IllegalAccessException e) {
//        e.printStackTrace();
//      }
//    }
//
//    return cardTransactionInfo;
//  }
//  private CardTransactionInfo createEntityFromRow(Row row) throws IllegalAccessException {
//    CardTransactionInfo cardTransactionInfo = new CardTransactionInfo();
//    cardTransactionInfo.setCif(row.getCell(0).getText());
//    cardTransactionInfo.setCardId(row.getCell(1).getText());
//    cardTransactionInfo.setCardNumber(row.getCell(2).getText());
//    cardTransactionInfo.setCustomerName(row.getCell(3).getText());
//    cardTransactionInfo.setProductId(row.getCell(4).getText());
//    cardTransactionInfo.setCardRank(row.getCell(5).getText());
//    cardTransactionInfo.setCardCategory(row.getCell(6).getText());
//    cardTransactionInfo.setCardLimit(row.getCell(7).getText());
//    cardTransactionInfo.setIssueOrganization(row.getCell(8).getText());
//    cardTransactionInfo.setPhoneNumber(row.getCell(9).getText());
//    cardTransactionInfo.setTotalTransaction(row.getCell(10).getText());
//    cardTransactionInfo.setTotalAmount(row.getCell(11).getText());
//    cardTransactionInfo.setMaturityDoubt(row.getCell(12).getText());
//    return cardTransactionInfo;
//  }


  private CardTransactionFile createCardTransactionFile(String fileName,
      StatusCardTransaction statusCard) {
    CardTransactionFile cardTransactionFile = new CardTransactionFile();
    cardTransactionFile.setName(fileName);
    cardTransactionFile.setStatusCard(statusCard);
    cardTransactionFileRepository.save(cardTransactionFile);
    return cardTransactionFile;
  }

}