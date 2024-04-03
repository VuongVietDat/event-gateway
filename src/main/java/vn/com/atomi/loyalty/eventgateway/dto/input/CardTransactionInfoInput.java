package vn.com.atomi.loyalty.eventgateway.dto.input;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CardTransactionInfoInput {

  @Schema(description = "Id bản ghi")
  @NotNull
  private Long id;

  @Schema(description = "Số cif")
  private String cif;

  @Schema(description = "Tên Khách hàng")
  private String customerName;

  @Schema(description = "Id thẻ")
  private String cardId;

  @Schema(description = "Mã sản phẩm")
  private String productId;

  @Schema(description = "Hạng thẻ")
  private String cardRank;

  @Schema(description = "Số thẻ")
  private String cardNumber;

  @Schema(description = "Loại thẻ")
  private String cardCategory;

  @Schema(description = "Hạn mức thẻ")
  private String cardLimit;

  @Schema(description = "Đơn vị phát hành")
  private String issueOrganization;

  @Schema(description = "Số lượng giao dịch")
  private String totalTransaction;

  @Schema(description = "Số  điện thoại")
  private String phoneNumber;

  @Schema(description = "Tổng số giao dịch")
  private String totalAmount;

  @Schema(description = "Nghi ngờ đáo hạn")
  private String maturityDoubt;

  @Schema(description = "Id file thẻ giao dịch")
  private Long cardTransactionFileId;

}
