package vn.com.atomi.loyalty.eventgateway.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.com.atomi.loyalty.base.data.BaseEntity;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "EG_CARD_TRANSACTION_INFO")
public class CardTransactionInfo extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "EG_CARD_TRANS_INFO_ID_SEQ")
  @SequenceGenerator(
      name = "EG_CARD_TRANS_INFO_ID_SEQ",
      sequenceName = "EG_CARD_TRANS_INFO_ID_SEQ",
      allocationSize = 1)
  private Long id;

  @Column(name = "CUSTOMER_NAME")
  private String customerName;

  @Column(name = "CARD_ID")
  private String cardId;

  @Column(name = "CARD_NUMBER")
  private String cardNumber;

  @Column(name = "CIF")
  private String cif;

  @Column(name = "PRODUCT_ID")
  private String productId;

  @Column(name = "CARD_RANK")
  private String cardRank;

  @Column(name = "CARD_CATEGORY")
  private String cardCategory;

  @Column(name = "CARD_LIMIT")
  private String cardLimit;

  @Column(name = "ISSUE_ORGANIZATION")
  private String issueOrganization;

  @Column(name = "PHONE_NUMBER")
  private String phoneNumber;

  @Column(name = "TOTAL_TRANSACTION")
  private String totalTransaction;

  @Column(name = "TOTAL_AMOUNT")
  private String totalAmount;

  @Column(name = "MATURITY_DOUBT")
  private String maturityDoubt;

  @Column(name = "CARD_TRANSACTION_FILE_ID")
  private Long cardTransactionFileId;
}
