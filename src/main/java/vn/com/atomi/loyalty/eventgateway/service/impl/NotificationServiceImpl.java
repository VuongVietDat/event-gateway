package vn.com.atomi.loyalty.eventgateway.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.apache.axis2.client.Options;
import org.apache.axis2.transport.http.HTTPConstants;
import org.springframework.stereotype.Service;
import vn.com.atomi.loyalty.base.data.BaseService;
import vn.com.atomi.loyalty.eventgateway.dto.input.NotificationInput;
import vn.com.atomi.loyalty.eventgateway.dto.output.NotificationOutput;
import vn.com.atomi.loyalty.eventgateway.service.NotificationService;
import vn.com.atomi.loyalty.eventgateway.utils.Constants;
import vn.com.atomi.loyalty.soapservice.NotificationServiceStub;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl extends BaseService implements NotificationService {

  @Value("${custom.properties.notification.service-url}")
  private String serviceEndpoint;

  @Value("${custom.properties.notification.overal-timeout}")
  private Integer overralTimeout;

  @Value("${custom.properties.notification.connection-timeout}")
  private Integer connectionTimeout;

  @Value("${custom.properties.notification.socket-timeout}")
  private Integer socketTimeout;

  private static NotificationServiceStub notificationStub = null;
  @Override
  public NotificationOutput sendNotification(NotificationInput notificationInput) {
    NotificationOutput notificationOutput = new NotificationOutput();
    try {
      // init notification stub
      this.initNotificationStub();
      // build notification request
      NotificationServiceStub.ReceiveNotification request = this.buildSendNotificationRequest(notificationInput);

      NotificationServiceStub.ReceiveNotificationResponse response = notificationStub.receiveNotification(request);
      if(response != null && response.get_return() != null) {
          notificationOutput.setRefId(response.get_return().getRefId());
          notificationOutput.setResultCode(response.get_return().getResultCode());
          notificationOutput.setResultDesc(response.get_return().getResultDesc());
      } else {
        // timeout
        notificationOutput.setResultCode(Constants.NotificationErrorCode.TIMEOUT);
        notificationOutput.setResultDesc("Timeout");
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return notificationOutput;
  }

  private NotificationServiceStub.ReceiveNotification buildSendNotificationRequest(NotificationInput request){
    NotificationServiceStub.ReceiveNotification response = new NotificationServiceStub.ReceiveNotification();
    NotificationServiceStub.NotificationServiceRequest notificationServiceRequest = new NotificationServiceStub.NotificationServiceRequest();
    try {
      NotificationServiceStub.Header2 requestHeader = new NotificationServiceStub.Header2();
      requestHeader.setLanguage(request.getLanguage());
      requestHeader.setClientTime(request.getClientTime());
      requestHeader.setClientTime(request.getClientTime());
      requestHeader.setRequestId(request.getRequestId());
      requestHeader.setTransTime(request.getTransTime());
      notificationServiceRequest.setHeader(requestHeader);
      notificationServiceRequest.setTitle(request.getTitle());
      notificationServiceRequest.setContent(request.getContent());
      notificationServiceRequest.setUserName(request.getUserName());
      response.setRequest(notificationServiceRequest);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return response;
  }

  private void initNotificationStub(){
    if(notificationStub == null){
      try {
        notificationStub = new NotificationServiceStub(serviceEndpoint);
        // Configure timeout options
        Options options = notificationStub._getServiceClient().getOptions();
        options.setTimeOutInMilliSeconds(overralTimeout); // Set overall timeout (in milliseconds)

        // Configure connection and socket timeout
        options.setProperty(HTTPConstants.CONNECTION_TIMEOUT, connectionTimeout); // Connection timeout (in milliseconds)
        options.setProperty(HTTPConstants.SO_TIMEOUT, socketTimeout);         // Socket timeout (in milliseconds)
      } catch (Exception ex) {
        ex.printStackTrace();
      }
    }
  }
}
