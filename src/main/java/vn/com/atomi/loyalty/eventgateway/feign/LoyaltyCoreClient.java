package vn.com.atomi.loyalty.eventgateway.feign;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import vn.com.atomi.loyalty.base.constant.RequestConstant;
import vn.com.atomi.loyalty.base.data.ResponseData;
import vn.com.atomi.loyalty.eventgateway.dto.output.CustomerOutput;
import vn.com.atomi.loyalty.eventgateway.feign.fallback.LoyaltyCoreClientFallbackFactory;

/**
 * @author haidv
 * @version 1.0
 */
@FeignClient(
    name = "loyalty-core-service",
    url = "${custom.properties.loyalty-core-service-url}",
    fallbackFactory = LoyaltyCoreClientFallbackFactory.class)
public interface LoyaltyCoreClient {

  @Operation(summary = "Api (nội bộ) lấy chi tiết thành viên theo mã ví")
  @GetMapping("/internal/customers")
  ResponseData<CustomerOutput> getCustomer(
      @RequestHeader(RequestConstant.REQUEST_ID) String requestId,
      @RequestParam String cifWallet,
      @RequestParam String cifBank);
}
