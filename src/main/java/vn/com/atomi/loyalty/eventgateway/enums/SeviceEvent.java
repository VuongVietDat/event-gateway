package vn.com.atomi.loyalty.eventgateway.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import vn.com.atomi.loyalty.base.event.CoreEvent;

@Getter
@AllArgsConstructor
public enum SeviceEvent implements CoreEvent {
  INSERT_CARD_TRANSACTION_INFO(
      "INSERT_CARD_TRANSACTION_INFO", "serviceDrivenEventListener", "saveCardTransactionInfo");

  private final String eventName;

  private final String handleEventBeanName;

  private final String handleEventFunctionName;
}
