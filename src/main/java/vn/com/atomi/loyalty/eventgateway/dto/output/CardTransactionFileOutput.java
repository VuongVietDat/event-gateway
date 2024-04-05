package vn.com.atomi.loyalty.eventgateway.dto.output;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import vn.com.atomi.loyalty.base.constant.DateConstant;
import vn.com.atomi.loyalty.base.data.BaseEntity;

import java.time.LocalDateTime;

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

    @Schema(description = "Thời gian thực hiện")
    @NotNull
    @JsonFormat(pattern = DateConstant.STR_PLAN_DD_MM_YYYY_HH_MM_SS_STROKE)
    private LocalDateTime createdAt;

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
