package vn.com.atomi.loyalty.eventgateway.dto.message;

import lombok.Getter;
import lombok.Setter;

/**
 * @author haidv
 * @version 1.0
 */
@Setter
@Getter
public class Lv24HTransactionMessage {

  private TransactionHeader transactionHeader;
  private TransactionFrom transactionFrom;
  private TransactionTo transactionTo;
  private TransactionAmount transactionAmount;
  private TransactionFee transactionFee;
  private TransactionCommission transactionCommission;
  private TransactionPromotion transactionPromotion;
  private TransactionInfo transactionInfo;
  private TransactionResponse transactionResponse;
  private TransactionStatus transactionStatus;
  private TransactionBill transactionBill;
}
