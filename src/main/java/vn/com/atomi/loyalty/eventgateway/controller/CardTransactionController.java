package vn.com.atomi.loyalty.eventgateway.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import vn.com.atomi.loyalty.base.annotations.DateTimeValidator;
import vn.com.atomi.loyalty.base.constant.DateConstant;
import vn.com.atomi.loyalty.base.data.BaseController;
import vn.com.atomi.loyalty.base.data.ResponseData;
import vn.com.atomi.loyalty.base.data.ResponsePage;
import vn.com.atomi.loyalty.base.data.ResponseUtils;
import vn.com.atomi.loyalty.base.security.Authority;
import vn.com.atomi.loyalty.eventgateway.dto.input.CardTransactionInfoInput;
import vn.com.atomi.loyalty.eventgateway.dto.output.CardTransactionFileOutput;
import vn.com.atomi.loyalty.eventgateway.dto.output.CardTransactionInfoOutput;
import vn.com.atomi.loyalty.eventgateway.service.CardTransactionService;

@RestController
@RequiredArgsConstructor
public class CardTransactionController extends BaseController {

  private final CardTransactionService cardTransactionService;

  @Operation(summary = "Api tải danh sách giao dịch thẻ")
  @PreAuthorize(Authority.CardTransaction.UPLOAD_TRANSACTION_FILE)
  @PostMapping("/card-transaction/upload-transaction-file")
  public ResponseEntity<ResponseData<Void>> uploadTransactionFile(
      @Parameter(description = "File danh sách upload giao dịch thẻ") @RequestParam
          MultipartFile transactionFile) {
    cardTransactionService.uploadTransactionFile(transactionFile);
    return ResponseUtils.success();
  }

  @Operation(summary = "Api chỉnh sửa record")
  @PreAuthorize(Authority.CardTransaction.UPDATE_CARD_TRANSACTION)
  @PutMapping("/update-card-transaction/")
  public ResponseEntity<ResponseData<Void>> updateCardTransaction(
      @Valid @RequestBody CardTransactionInfoInput cardTransactionInfoInput) {
    cardTransactionService.updateCardTransaction(cardTransactionInfoInput);
    return ResponseUtils.success();
  }

  @Operation(summary = "Api lấy thông tin file giao dịch thẻ")
  @PreAuthorize(Authority.CardTransaction.DETAIL_TRANSACTION_FILE)
  @GetMapping("/card-transaction/file/detail")
  public ResponseEntity<ResponseData<CardTransactionFileOutput>> detailCardTransactionFile(
      @Parameter(description = "Id bản ghi file transaction") @RequestParam Long id) {
    return ResponseUtils.success(cardTransactionService.getDetailCardTransaction(id));
  }

  @Operation(summary = "Api lấy danh sách chi tiết giao dịch thẻ")
  @PreAuthorize(Authority.CardTransaction.DETAIL_TRANSACTION_INFO)
  @GetMapping("/card-transaction/info/{id}")
  public ResponseEntity<ResponseData<ResponsePage<CardTransactionInfoOutput>>>
      getListTransactionInfo(
          @Parameter(description = "Số trang, bắt đầu từ 1", example = "1") @RequestParam
              Integer pageNo,
          @Parameter(description = "Số lượng bản ghi 1 trang, tối đa 200", example = "10")
              @RequestParam
              Integer pageSize,
          @Parameter(description = "Sắp xếp, Pattern: ^[a-z0-9]+:(asc|desc)")
              @RequestParam(required = false)
              String sort,
          @Parameter(description = "Id bản ghi file giao dịch thẻ") @RequestParam Long id) {
    return ResponseUtils.success(
        cardTransactionService.getDetailCardTransactionInfo(
            id, super.pageable(pageNo, pageSize, sort)));
  }

  @Operation(summary = "Api tra cứu thông tin giao dịch thẻ")
  @PreAuthorize(Authority.CardTransaction.LIST_TRANSACTION_FILE)
  @GetMapping("/card-transaction/file/list")
  public ResponseEntity<ResponseData<ResponsePage<CardTransactionFileOutput>>>
      getListTransactionFile(
          @Parameter(description = "Số trang, bắt đầu từ 1", example = "1") @RequestParam
              Integer pageNo,
          @Parameter(description = "Số lượng bản ghi 1 trang, tối đa 200", example = "10")
              @RequestParam
              Integer pageSize,
          @Parameter(description = "Sắp xếp, Pattern: ^[a-z0-9]+:(asc|desc)")
              @RequestParam(required = false)
              String sort,
          @Parameter(description = "Id bản ghi file giao dịch thẻ") @RequestParam(required = false)
              Long id,
          @Parameter(
                  description = "Thời gian giao dịch từ ngày (dd/MM/yyyy)",
                  example = "01-APR-24")
              @DateTimeValidator(
                  required = false,
                  pattern = DateConstant.STR_PLAN_DD_MM_YYYY_STROKE)
              @RequestParam(required = false)
              Date startTransactionDate,
          @Parameter(
                  description = "Thời gian giao dịch đến ngày (dd/MM/yyyy)",
                  example = "01-APR-24")
              @DateTimeValidator(
                  required = false,
                  pattern = DateConstant.STR_PLAN_DD_MM_YYYY_STROKE)
              @RequestParam(required = false)
              Date endTransactionDate,
          @Parameter(description = "Trạng thái giao dịch thẻ") @RequestParam(required = false)
              String statusCard,
          @Parameter(description = "Người thực hiện") @RequestParam(required = false)
              String createdBy) {

    return ResponseUtils.success(
        cardTransactionService.getListTransactionFile(
            id,
            startTransactionDate,
            endTransactionDate,
            statusCard,
            createdBy,
            super.pageable(pageNo, pageSize, sort)));
  }
}
