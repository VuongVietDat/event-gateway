package vn.com.atomi.loyalty.eventgateway.repository.redis;

import java.util.List;
import vn.com.atomi.loyalty.eventgateway.entity.CardTransactionInfo;

public interface CustomRepository {

  void saveAllCardTransactionInfos(List<CardTransactionInfo> batch);
}
