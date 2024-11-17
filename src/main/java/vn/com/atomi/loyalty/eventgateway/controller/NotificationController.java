package vn.com.atomi.loyalty.eventgateway.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import vn.com.atomi.loyalty.base.data.BaseController;
import vn.com.atomi.loyalty.base.data.ResponseData;
import vn.com.atomi.loyalty.base.data.ResponseUtils;
import vn.com.atomi.loyalty.base.security.Authority;
import vn.com.atomi.loyalty.eventgateway.dto.input.NotificationInput;
import vn.com.atomi.loyalty.eventgateway.dto.output.NotificationOutput;
import vn.com.atomi.loyalty.eventgateway.service.NotificationService;

@RestController
@RequiredArgsConstructor
public class NotificationController extends BaseController {
  
  private final NotificationService notificationService;

  @Operation(summary = "Api gửi thông báo tới khách hàng")
  @PreAuthorize(Authority.Notification.SEND_NOTIFICATION)
  @PostMapping("/notification")
  public ResponseEntity<ResponseData<NotificationOutput>> sendNotification(
          @Valid @RequestBody NotificationInput request) {
    return ResponseUtils.success(notificationService.sendNotification(request));
  }
}
