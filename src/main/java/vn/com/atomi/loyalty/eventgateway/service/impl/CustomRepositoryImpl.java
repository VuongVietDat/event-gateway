package vn.com.atomi.loyalty.eventgateway.service.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import vn.com.atomi.loyalty.eventgateway.entity.CardTransactionInfo;
import vn.com.atomi.loyalty.eventgateway.repository.redis.CustomRepository;

@Repository
@RequiredArgsConstructor
public class CustomRepositoryImpl implements CustomRepository {

  private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

  private final EntityManager entityManager;
  @Override
  public void saveAllCardTransactionInfos(List<CardTransactionInfo> batch) {
    String saveCardInfos = "INSERT INTO card_transaction_info (cif, card_id, card_number, " +
        "customer_name, product_id, card_rank, card_category, card_limit, " +
        "issue_organization, phone_number, total_transaction, total_amount, " +
        "maturity_doubt) VALUES ";

    for (int i = 0; i < batch.size(); i++) {
      saveCardInfos += "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
      if (i < batch.size() - 1) {
        saveCardInfos += ",";
      }
    }

    Query query = entityManager.createNativeQuery(saveCardInfos);

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
