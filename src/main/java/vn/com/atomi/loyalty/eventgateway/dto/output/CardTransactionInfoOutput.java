package vn.com.atomi.loyalty.eventgateway.dto.output;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import vn.com.atomi.loyalty.eventgateway.entity.CardTransactionInfo;
import vn.com.atomi.loyalty.eventgateway.enums.Status;

@Getter
@Setter
public class CardTransactionInfoOutput {

  @Schema(description = "Id bản ghi")
  @NotNull
  private Long id;

  @Schema(description = "Số cif")
  @NotBlank
  private String cif;

  @Schema(description = "Tên Khách hàng")
  @NotBlank
  private String customerName;

  @Schema(description = "Id thẻ")
  @NotBlank
  private String cardId;

  @Schema(description = "Số thẻ")
  @NotBlank
  private String cardNumber;

  @Schema(description = "Mã sản phẩm")
  @NotBlank
  private String productId;

  @Schema(description = "Hạng thẻ")
  @NotBlank
  private String cardRank;

  @Schema(description = "Loại thẻ")
  @NotBlank
  private String cardCategory;

  @Schema(description = "Hạn mức thẻ")
  @NotBlank
  private String cardLimit;

  @Schema(description = "Đơn vị phát hành")
  @NotBlank
  private String issueOrganization;

  @Schema(description = "Số lượng giao dịch")
  @NotBlank
  private String totalTransaction;

  @Schema(description = "Số  điện thoại")
  @NotBlank
  private String phoneNumber;

  @Schema(description = "Tổng số giao dịch")
  @NotBlank
  private String totalAmount;

  @Schema(description = "Nghi ngờ đáo hạn")
  @NotBlank
  private String maturityDoubt;

  @Schema(description = "Id của file transaction")
  @NotNull
  private Long cardTransactionFileId;

  @Schema(description = "Người tạo")
  private String creator;

  @Schema(description = "Trạng thái")
  private Status status;

  @Schema(description = "Số tham chiếu")
  private String refNo;
}
