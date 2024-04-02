package vn.com.atomi.loyalty.eventgateway.dto.message;

import lombok.Getter;
import lombok.Setter;

/**
 * @author haidv
 * @version 1.0
 */
@Setter
@Getter
public class TransactionFee {

  private String feeAmount;
  private String feeCurrency;
  private String feeAmountRate;
  private String feeInd;
}
