package vn.com.atomi.loyalty.eventgateway.entity;

import jakarta.persistence.*;
import lombok.*;
import vn.com.atomi.loyalty.base.data.BaseEntity;

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
}
