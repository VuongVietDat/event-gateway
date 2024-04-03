package vn.com.atomi.loyalty.eventgateway.feign;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import vn.com.atomi.loyalty.base.constant.RequestConstant;
import vn.com.atomi.loyalty.base.data.ResponseData;
import vn.com.atomi.loyalty.eventgateway.dto.output.Lv24ProductDataMapOutput;
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

  @Operation(summary = "Api (nội bộ) lấy cấu hình chuyển sản phẩm LV24H thành loyalty transaction")
  @GetMapping("/internal/lv24h-map-product")
  ResponseData<Lv24ProductDataMapOutput> getLv24MapProduct(
      @RequestHeader(RequestConstant.REQUEST_ID) String requestId, @RequestParam String productId);
}
