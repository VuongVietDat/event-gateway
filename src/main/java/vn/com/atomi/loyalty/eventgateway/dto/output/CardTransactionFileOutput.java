package vn.com.atomi.loyalty.eventgateway.dto.output;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import vn.com.atomi.loyalty.base.data.BaseEntity;

@Getter
@Setter
public class CardTransactionFileOutput extends BaseEntity {

  @Schema(description = "Id bản ghi file giao dịch thẻ")
  @NotNull
  private Long id;

  @Schema(description = "Tên file giao dịch thẻ")
  @NotNull
  private String name;

  @Schema(description = "Trạng thái file giao dịch thẻ")
  @NotNull
  private String statusCard;


}
