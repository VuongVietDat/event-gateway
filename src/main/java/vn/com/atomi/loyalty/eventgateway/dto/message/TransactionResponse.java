package vn.com.atomi.loyalty.eventgateway.dto.message;

import lombok.Getter;
import lombok.Setter;

/**
 * @author haidv
 * @version 1.0
 */
@Setter
@Getter
public class TransactionResponse {

  private String responseTime;
  private String responseCode;
  private String responseMessage;
  private String responseStatus;
  private String errorCode;
  private String transResponse;
}
