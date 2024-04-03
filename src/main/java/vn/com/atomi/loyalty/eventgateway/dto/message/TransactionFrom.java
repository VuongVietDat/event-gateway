package vn.com.atomi.loyalty.eventgateway.dto.message;

import lombok.Getter;
import lombok.Setter;

/**
 * @author haidv
 * @version 1.0
 */
@Setter
@Getter
public class TransactionFrom {

  private String fromSystemId;
  private String fromCustNo;
  private String fromAccountClass;
  private String fromAccountType;
  private String fromAccountNo;
  private String fromAccountCurrency;
  private String fromAccountFullname;
  private String fromMessage;
}
