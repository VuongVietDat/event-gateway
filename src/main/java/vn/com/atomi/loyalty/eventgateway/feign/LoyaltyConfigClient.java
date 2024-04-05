package vn.com.atomi.loyalty.eventgateway.feign;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.com.atomi.loyalty.base.constant.RequestConstant;
import vn.com.atomi.loyalty.base.data.ResponseData;
import vn.com.atomi.loyalty.base.data.ResponseUtils;
import vn.com.atomi.loyalty.base.security.Authority;
import vn.com.atomi.loyalty.eventgateway.dto.output.ProductLineOutput;
import vn.com.atomi.loyalty.eventgateway.dto.output.SourceDataMapOutput;
import vn.com.atomi.loyalty.eventgateway.enums.SourceGroup;
import vn.com.atomi.loyalty.eventgateway.feign.fallback.LoyaltyConfigClientFallbackFactory;

import java.util.List;

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

  @Operation(summary = "Api (nội bộ) lấy cấu hình chuyển data nguồn thành loyalty data")
  @GetMapping("/internal/source-data-map-all")
  ResponseData<List<SourceDataMapOutput>> getAllSourceDataMap(
      @RequestHeader(RequestConstant.REQUEST_ID) String requestId,
      @RequestParam SourceGroup sourceGroup);

  @Operation(summary = "Api (nội bộ) lấy danh sách danh mục dòng sản phẩm dịch vụ")
  @GetMapping("/internal/master-data/product-lines")
  ResponseData<List<ProductLineOutput>> getProductLines(@RequestHeader(RequestConstant.REQUEST_ID) String requestId);
}
