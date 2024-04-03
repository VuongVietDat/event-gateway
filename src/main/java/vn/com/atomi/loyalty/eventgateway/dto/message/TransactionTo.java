package vn.com.atomi.loyalty.eventgateway.dto.message;

import lombok.Getter;
import lombok.Setter;

/**
 * @author haidv
 * @version 1.0
 */
@Setter
@Getter
public class TransactionTo {

  private String toSystemId;
  private String toCustNo;
  private String toAccountNo;
  private String toAccountCurrency;
  private String toAmount;
  private String toAccountFullname;
  private String toUserName;
}
