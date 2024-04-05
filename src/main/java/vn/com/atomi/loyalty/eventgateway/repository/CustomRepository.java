package vn.com.atomi.loyalty.eventgateway.repository;

import java.util.List;
import vn.com.atomi.loyalty.eventgateway.entity.CardTransactionInfo;

public interface CustomRepository {

  void saveAllCardTransactionInfos(List<CardTransactionInfo> batch, Long cardTransactionFileId);
}
