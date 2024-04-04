package vn.com.atomi.loyalty.eventgateway.feign;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import vn.com.atomi.loyalty.base.constant.RequestConstant;
import vn.com.atomi.loyalty.base.data.ResponseData;
import vn.com.atomi.loyalty.eventgateway.dto.output.SourceDataMapOutput;
import vn.com.atomi.loyalty.eventgateway.enums.SourceGroup;
import vn.com.atomi.loyalty.eventgateway.feign.fallback.LoyaltyConfigClientFallbackFactory;

/**
 * @author haidv
 * @version 1.0
 */
@FeignClient(
    name = "loyalty-config-service",
    url = "${custom.properties.loyalty-config-service-url}",
    fallbackFactory = LoyaltyConfigClientFallbackFactory.class)
public interface LoyaltyConfigClient {

  @Operation(summary = "Api (nội bộ) lấy cấu hình chuyển data nguồn thành loyalty data")
  @GetMapping("/internal/source-data-map")
  ResponseData<SourceDataMapOutput> getSourceDataMap(
      @RequestHeader(RequestConstant.REQUEST_ID) String requestId,
      @RequestParam String sourceId,
      @RequestParam String sourceType,
      @RequestParam SourceGroup sourceGroup);
}
