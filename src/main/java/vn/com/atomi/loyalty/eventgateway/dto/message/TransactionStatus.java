package vn.com.atomi.loyalty.eventgateway.dto.message;

import lombok.Getter;
import lombok.Setter;

/**
 * @author haidv
 * @version 1.0
 */
@Setter
@Getter
public class TransactionStatus {

  private String approveStatus;
  private String transStatus;
  private String transProgress;
  private String paymentStatus;
}
