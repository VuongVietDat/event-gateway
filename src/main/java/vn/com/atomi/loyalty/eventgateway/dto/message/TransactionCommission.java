package vn.com.atomi.loyalty.eventgateway.dto.message;

import lombok.Getter;
import lombok.Setter;

/**
 * @author haidv
 * @version 1.0
 */
@Setter
@Getter
public class TransactionCommission {

  private String commissionAmount;
  private String commissionCurrency;
  private String commissionRate;
  private String commissionInd;
}
