package vn.com.atomi.loyalty.eventgateway.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

import org.dhatim.fastexcel.reader.Cell;
import org.dhatim.fastexcel.reader.ReadableWorkbook;
import org.dhatim.fastexcel.reader.Row;
import org.dhatim.fastexcel.reader.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vn.com.atomi.loyalty.base.data.BaseService;
import vn.com.atomi.loyalty.base.data.ResponseData;
import vn.com.atomi.loyalty.base.exception.BaseException;
import vn.com.atomi.loyalty.eventgateway.dto.input.CardTransactionInfoInput;
import vn.com.atomi.loyalty.eventgateway.entity.CardTransactionFile;
import vn.com.atomi.loyalty.eventgateway.entity.CardTransactionInfo;
import vn.com.atomi.loyalty.eventgateway.enums.ErrorCode;
import vn.com.atomi.loyalty.eventgateway.enums.StatusCardTransaction;
import vn.com.atomi.loyalty.eventgateway.repository.redis.CardTransactionFileRepository;
import vn.com.atomi.loyalty.eventgateway.repository.redis.CardTransactionInfoRepository;
import vn.com.atomi.loyalty.eventgateway.service.CardTransactionService;

@Service
@RequiredArgsConstructor
public class CardTransactionServiceImpl extends BaseService implements CardTransactionService {

  private final CardTransactionInfoRepository cardTransactionInfoRepository;

  @Autowired
  private CardTransactionFileRepository cardTransactionFileRepository;

  @Override
  public Object uploadTransactionFile(MultipartFile transactionFile) throws IOException {
    List<CardTransactionInfo> batch = new ArrayList<>();
    int batchSize = 100;
    int count = 0;
    String fileName = transactionFile.getOriginalFilename();
    CardTransactionFile cardTransactionFile = createCardTransactionFile(fileName);
    cardTransactionFileRepository.save(cardTransactionFile);
    InputStream inputStream = transactionFile.getInputStream();
    ReadableWorkbook workbook = new ReadableWorkbook(inputStream);
    Sheet sheet = workbook.getFirstSheet();
    Iterator<Row> rowIterator = sheet.read().iterator();
    boolean isFirstRow = true;
    while (rowIterator.hasNext()) {
      Row row = rowIterator.next();
      if (isFirstRow) {
        isFirstRow = false;
        continue;
      }
      if (row != null) {
        boolean isRowEmpty = true;
        Iterator<Cell> cellIterator = row.iterator();
        int columnCount = 0;
        while (cellIterator.hasNext() && columnCount < 13) {
          Cell cell = cellIterator.next();
          if (cell != null && cell.getText() != null && !cell.getText().isEmpty()) {
            isRowEmpty = false;
          }
          columnCount++;
        }
        if (!isRowEmpty) {
          CardTransactionInfo cardTransactionInfo = createEntityFromRow(row);
          if (cardTransactionInfo != null) {
            cardTransactionInfo.setCardTransactionFileId(cardTransactionFile.getId());
            batch.add(cardTransactionInfo);
            count++;
            if (count % batchSize == 0) {
              // Save 100 records to the database and then reset the list
              cardTransactionInfoRepository.saveAll(batch);
              batch.clear();
            }
          }
        }
      }
    }
    if (!batch.isEmpty()) {
      cardTransactionInfoRepository.saveAll(batch);
    }

    return new ResponseData<>();
  }

  @Override
  public void updateCardTransaction(CardTransactionInfoInput cardTransactionInfoInput) {
    Optional<CardTransactionInfo> cardTransactionInfoOptional =
        cardTransactionInfoRepository.findById(cardTransactionInfoInput.getId());
    cardTransactionInfoOptional.ifPresent(cardTransactionInfo -> {
      if (cardTransactionInfoInput.getCif() != null ) {
        cardTransactionInfo.setCif(cardTransactionInfoInput.getCif());
      }
      if (cardTransactionInfoInput.getCardNumber() != null ) {
        cardTransactionInfo.setCardNumber(cardTransactionInfoInput.getCardNumber());
      }
      if (cardTransactionInfoInput.getCustomerName() != null ) {
        cardTransactionInfo.setCustomerName(cardTransactionInfoInput.getCustomerName());
      }
      if (cardTransactionInfoInput.getProductId() != null ) {
        cardTransactionInfo.setProductId(cardTransactionInfoInput.getProductId());
      }
      if (cardTransactionInfoInput.getCardRank() != null ) {
        cardTransactionInfo.setCardRank(cardTransactionInfoInput.getCardRank());
      }
      if (cardTransactionInfoInput.getCardCategory() != null ) {
        cardTransactionInfo.setCardCategory(cardTransactionInfoInput.getCardCategory());
      }
      if (cardTransactionInfoInput.getCardLimit() != null ) {
        cardTransactionInfo.setCardLimit(cardTransactionInfoInput.getCardLimit());
      }
      if (cardTransactionInfoInput.getIssueOrganization() != null ) {
        cardTransactionInfo.setIssueOrganization(cardTransactionInfoInput.getIssueOrganization());
      }
      if (cardTransactionInfoInput.getPhoneNumber() != null ) {
        cardTransactionInfo.setPhoneNumber(cardTransactionInfoInput.getPhoneNumber());
      }
      if (cardTransactionInfoInput.getTotalTransaction() != null ) {
        cardTransactionInfo.setTotalTransaction(cardTransactionInfoInput.getTotalTransaction());
      }
      if (cardTransactionInfoInput.getTotalAmount() != null ) {
        cardTransactionInfo.setTotalAmount(cardTransactionInfoInput.getTotalAmount());
      }
      if (cardTransactionInfoInput.getMaturityDoubt() != null ) {
        cardTransactionInfo.setMaturityDoubt(cardTransactionInfoInput.getMaturityDoubt());
      }
      cardTransactionInfoRepository.save(cardTransactionInfo);
    });
  }

  private CardTransactionInfo createEntityFromRow(Row row) {
    CardTransactionInfo cardTransactionInfo = new CardTransactionInfo();
    cardTransactionInfo.setCif(row.getCell(0).getText());
    cardTransactionInfo.setCardId(row.getCell(1).getText());
    cardTransactionInfo.setCardNumber(row.getCell(2).getText());
    cardTransactionInfo.setCustomerName(row.getCell(3).getText());
    cardTransactionInfo.setProductId(row.getCell(4).getText());
    cardTransactionInfo.setCardRank(row.getCell(5).getText());
    cardTransactionInfo.setCardCategory(row.getCell(6).getText());
    cardTransactionInfo.setCardLimit(row.getCell(7).getText());
    cardTransactionInfo.setIssueOrganization(row.getCell(8).getText());
    cardTransactionInfo.setPhoneNumber(row.getCell(9).getText());
    cardTransactionInfo.setTotalTransaction(row.getCell(10).getText());
    cardTransactionInfo.setTotalAmount(row.getCell(11).getText());
    cardTransactionInfo.setMaturityDoubt(row.getCell(12).getText());
    return cardTransactionInfo;
  }

  private CardTransactionFile createCardTransactionFile(String fileName) {
    CardTransactionFile cardTransactionFile = new CardTransactionFile();
    cardTransactionFile.setName(fileName);
    cardTransactionFile.setStatusCard(String.valueOf(StatusCardTransaction.INITIALIZING));
    return cardTransactionFile;
  }

}