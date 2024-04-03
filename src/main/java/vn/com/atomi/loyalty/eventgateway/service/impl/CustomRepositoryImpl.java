package vn.com.atomi.loyalty.eventgateway.service.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import vn.com.atomi.loyalty.eventgateway.entity.CardTransactionInfo;
import vn.com.atomi.loyalty.eventgateway.repository.CustomRepository;

@Repository
@RequiredArgsConstructor
public class CustomRepositoryImpl implements CustomRepository {

  private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

  private final EntityManager entityManager;

  @Transactional
  @Override
  public void saveAllCardTransactionInfos(
      List<CardTransactionInfo> batch, Long cardTransactionFileId) {
    StringBuilder saveCardInfos = new StringBuilder("INSERT ALL ");

    for (CardTransactionInfo card : batch) {
      saveCardInfos
          .append("INTO EG_CARD_TRANSACTION_INFO (id, cif, card_id, card_number, ")
          .append("customer_name, product_id, card_rank, card_category, card_limit, ")
          .append("issue_organization, phone_number, total_transaction, total_amount, ")
          .append(
              "maturity_doubt , card_transaction_file_id , is_deleted) VALUES (GET_CTI_ID_SEQ(), ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,")
          .append(cardTransactionFileId)
          .append(",'0'")
          .append(")");
    }

    saveCardInfos.append(" SELECT * FROM DUAL");

    Query query = entityManager.createNativeQuery(saveCardInfos.toString().formatted());

    int parameterIndex = 1;
    for (CardTransactionInfo card : batch) {
      query.setParameter(parameterIndex++, card.getCif());
      query.setParameter(parameterIndex++, card.getCardId());
      query.setParameter(parameterIndex++, card.getCardNumber());
      query.setParameter(parameterIndex++, card.getCustomerName());
      query.setParameter(parameterIndex++, card.getProductId());
      query.setParameter(parameterIndex++, card.getCardRank());
      query.setParameter(parameterIndex++, card.getCardCategory());
      query.setParameter(parameterIndex++, card.getCardLimit());
      query.setParameter(parameterIndex++, card.getIssueOrganization());
      query.setParameter(parameterIndex++, card.getPhoneNumber());
      query.setParameter(parameterIndex++, card.getTotalTransaction());
      query.setParameter(parameterIndex++, card.getTotalAmount());
      query.setParameter(parameterIndex++, card.getMaturityDoubt());
    }

    query.executeUpdate();
  }
}
