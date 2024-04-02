package vn.com.atomi.loyalty.eventgateway.dto.message;

import lombok.Getter;
import lombok.Setter;

/**
 * @author haidv
 * @version 1.0
 */
@Setter
@Getter
public class TransactionAmount {

  private String amountTransfer;
  private String amountCurrency;
  private String amountRate;
  private String discountAmount;
  private String vatAmount;
  private String inputAmount;
  private String partnerDiscountAmount;
}
