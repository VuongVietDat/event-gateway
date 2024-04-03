package vn.com.atomi.loyalty.eventgateway.service;

import vn.com.atomi.loyalty.eventgateway.dto.message.Lv24HTransactionMessage;

/**
 * @author haidv
 * @version 1.0
 */
public interface HandlerTransactionEventService {

  void makeLv24hTransactionHandle(Lv24HTransactionMessage lv24HTransactionMessage);
}
