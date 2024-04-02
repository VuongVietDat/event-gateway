package vn.com.atomi.loyalty.eventgateway.dto.message;

import lombok.Getter;
import lombok.Setter;

/**
 * @author haidv
 * @version 1.0
 */
@Setter
@Getter
public class TransactionHeader {

  private String rowID;
  private String transID;
  private String transCode;
  private String userID;
  private String userName;
  private String transTime;
  private String actionID;
  private String transType;
  private String channelReceiver;
  private String stakUserTypeDo;
  private String userDo;
  private String branchCodeDo;
  private String productID;
  private String businessProcess;
  private String originalRequestID;
  private String makerID;
  private String makerDate;
  private String checkerDate;
  private String requestID;
  private String sourceType;
  private String categoryCode;
  private String custNoDo;
}
