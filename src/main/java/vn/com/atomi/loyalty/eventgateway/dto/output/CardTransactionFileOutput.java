package vn.com.atomi.loyalty.eventgateway.dto.output;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CardTransactionFileOutput {

  @Schema(description = "Id bản ghi file giao dịch thẻ")
  @NotNull
  private Long id;

  @Schema(description = "Tên file giao dịch thẻ")
  @NotNull
  private String name;

  @Schema(description = "Trạng thái file giao dịch thẻ")
  @NotNull
  private String statusCard;

  @Schema(description = "Người thực hiện")
  @NotNull
  private String createdBy;

  @Schema(description = "Tổng số bản ghi upload")
  @NotNull
  private String totalRecord;

  @Schema(description = "Tổng số bản ghi hợp lệ")
  @NotNull
  private String totalRecordSuccessful;

  @Schema(description = "Tổng số bản ghi không hợp lệ")
  @NotNull
  private String totalRecordFailed;

  @Schema(description = "Tổng số tiền giao dịch thẻ")
  @NotNull
  private String totalTransactionMoney;
}
