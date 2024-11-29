package vn.com.atomi.loyalty.eventgateway.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.com.atomi.loyalty.base.annotations.DateTimeValidator;
import vn.com.atomi.loyalty.base.constant.DateConstant;
import vn.com.atomi.loyalty.base.constant.RequestConstant;
import vn.com.atomi.loyalty.base.data.BaseController;
import vn.com.atomi.loyalty.base.data.ResponseData;
import vn.com.atomi.loyalty.base.data.ResponseUtils;
import vn.com.atomi.loyalty.base.security.Authority;
import vn.com.atomi.loyalty.eventgateway.dto.output.EGCBiometricOutput;
import vn.com.atomi.loyalty.eventgateway.repository.EGComBiometricRepository;
import vn.com.atomi.loyalty.eventgateway.service.EGComBiometricService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class EGCBiometricController extends BaseController {

    private final EGComBiometricService egComBiometricService;

    @Operation(summary = "Api (noi bo) lấy danh sách khách hàng hoàn thiện sinh trắc học")
    @PreAuthorize(Authority.ROLE_SYSTEM)
    @GetMapping("/internal/completebiometric")
    public ResponseEntity<ResponseData<List<EGCBiometricOutput>>> getLstCompleteBiometric(
            @Parameter(description = "Chuỗi xác thực khi gọi api nội bộ",
                    example = "eb6b9f6fb84a45d9c9b2ac5b2c5bac4f36606b13abcb9e2de01fa4f066968cd0")
            @RequestHeader(RequestConstant.SECURE_API_KEY) @SuppressWarnings("unused") String apiKey,
            @Parameter(
                    description = "Thời gian lay casa từ ngày (dd/MM/yyyy)",
                    example = "01/01/2024")
            @DateTimeValidator(
                    required = false,
                    pattern = DateConstant.STR_PLAN_DD_MM_YYYY_STROKE)
            @RequestParam(required = false)
            String startDate,
            @Parameter(
                    description = "Thời gian lay casa đến ngày (dd/MM/yyyy)",
                    example = "31/12/2024")
            @DateTimeValidator(
                    required = false,
                    pattern = DateConstant.STR_PLAN_DD_MM_YYYY_STROKE)
            @RequestParam(required = false)
            String endDate) {
        return ResponseUtils.success(egComBiometricService.getListEGComBiometrics());
    }

    @Operation(
            summary = "Api (nội bộ) tự động chuyển trạng thái đã cộng điểm hoàn thành sinh trắc học cho khách hàng")
    @PreAuthorize(Authority.ROLE_SYSTEM)
    @PutMapping("/internal/completebiometric/update")
    public ResponseEntity<ResponseData<Void>> automaticupdate(
            @Parameter(
                    description = "Chuỗi xác thực khi gọi api nội bộ",
                    example = "eb6b9f6fb84a45d9c9b2ac5b2c5bac4f36606b13abcb9e2de01fa4f066968cd0")
            @RequestHeader(RequestConstant.SECURE_API_KEY)
            @SuppressWarnings("unused")
            String apiKey,
            @Parameter(description = "CifBank của khách hàng")
            @RequestParam String cifBank) {
        egComBiometricService.updateEGComBioMetric(cifBank);
        return ResponseUtils.success();
    }
}
