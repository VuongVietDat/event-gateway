package vn.com.atomi.loyalty.eventgateway.dto.message;

import lombok.Getter;
import lombok.Setter;

/**
 * @author haidv
 * @version 1.0
 */
@Setter
@Getter
public class TransactionPromotion {

  private String promotionAmount;
  private String promotionCurrency;
  private String promotionRate;
}
