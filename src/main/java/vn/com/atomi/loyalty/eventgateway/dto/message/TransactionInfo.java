package vn.com.atomi.loyalty.eventgateway.dto.message;

import lombok.Getter;
import lombok.Setter;

/**
 * @author haidv
 * @version 1.0
 */
@Setter
@Getter
public class TransactionInfo {

  private String transName;
  private String transDesc;
  private String requestTime;
  private String requestChannel;
  private String transRequest;
  private String receiveTime;
  private String insertTime;
  private String finishTime;
  private String inputType;
  private String inputValue;
}
