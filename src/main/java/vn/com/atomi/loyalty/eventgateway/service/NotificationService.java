package vn.com.atomi.loyalty.eventgateway.service;

import org.springframework.http.ResponseEntity;
import vn.com.atomi.loyalty.base.data.ResponseData;
import vn.com.atomi.loyalty.eventgateway.dto.input.NotificationInput;
import vn.com.atomi.loyalty.eventgateway.dto.output.NotificationOutput;

public interface NotificationService {

  NotificationOutput sendNotification(NotificationInput request);
}
