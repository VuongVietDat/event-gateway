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

  private String rowId;
  private String transId;
  private String transCode;
  private String userId;
  private String userName;
  private String transTime;
  private String actionId;
  private String transType;
  private String channelReceiver;
  private String stakUserTypeDo;
  private String userDo;
  private String branchCodeDo;
  private String productId;
  private String businessProcess;
  private String originalRequestId;
  private String makerId;
  private String makerDate;
  private String checkerDate;
  private String requestId;
  private String sourceType;
  private String categoryCode;
  private String custNoDo;
}
