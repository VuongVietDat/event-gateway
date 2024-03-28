package vn.com.atomi.loyalty.eventgateway.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import vn.com.atomi.loyalty.base.data.BaseController;
import vn.com.atomi.loyalty.base.data.ResponseData;
import vn.com.atomi.loyalty.base.data.ResponseUtils;
import vn.com.atomi.loyalty.eventgateway.dto.input.CardTransactionInfoInput;
import vn.com.atomi.loyalty.eventgateway.service.CardTransactionService;

@RestController
@RequiredArgsConstructor
public class CardTransactionController extends BaseController {

  private final CardTransactionService cardTransactionService;

  @Operation(summary = "Api tải danh sách giao dịch thẻ")
  @PostMapping("/card-transaction/upload-transaction-file")
  public ResponseEntity<ResponseData<Object>>
  uploadTransactionFile(
      @Parameter(description = "File danh sách upload giao dịch thẻ") @RequestParam MultipartFile transactionFile
  ) throws IOException {
    return ResponseUtils.success(cardTransactionService.uploadTransactionFile(transactionFile));
  }

  @Operation(summary = "Api chỉnh sửa record theo id")
//  @PreAuthorize(Authority.CustomerGroup.UPDATE_CUSTOMER_GROUP)
  @PutMapping("/card-transaction/update")
  public ResponseEntity<ResponseData<Void>> updateCardTransaction(
      @Valid @RequestBody CardTransactionInfoInput cardTransactionInfoInput) {
    cardTransactionService.updateCardTransaction(cardTransactionInfoInput);
    return ResponseUtils.success();
  }


}
