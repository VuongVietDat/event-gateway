package vn.com.atomi.loyalty.eventgateway.entity;

import jakarta.persistence.*;
import lombok.*;
import vn.com.atomi.loyalty.base.data.BaseEntity;
import vn.com.atomi.loyalty.eventgateway.enums.StatusCardTransaction;

/**
 * @author haidv
 * @version 1.0
 */
@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "EG_CARD_TRANSACTION_FILE")
public class CardTransactionFile extends BaseEntity {

  @Id
  @Column(name = "ID")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "EG_CARD_TRANS_FILE_ID_SEQ")
  @SequenceGenerator(
      name = "EG_CARD_TRANS_FILE_ID_SEQ",
      sequenceName = "EG_CARD_TRANS_FILE_ID_SEQ",
      allocationSize = 1)
  private Long id;

  @Column(name = "STATUS_CARD")
  @Enumerated(EnumType.STRING)
  private StatusCardTransaction statusCard;

  @Column(name = "NAME")
  private String name;

  @Column(name = "TOTAL_RECORD_SUCCESSFUL")
  private int totalRecordSuccessful;

  @Column(name = "TOTAL_RECORD_FAILED")
  private int totalRecordFailed;

  @Column(name = "TOTAL_TRANSACTION_MONEY")
  private String totalTransactionMoney;
}
