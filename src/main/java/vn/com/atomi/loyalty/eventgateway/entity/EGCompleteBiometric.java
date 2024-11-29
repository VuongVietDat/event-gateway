package vn.com.atomi.loyalty.eventgateway.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.Nationalized;
import vn.com.atomi.loyalty.base.data.BaseEntity;

import java.util.Date;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "EG_COMPLETE_BIOMETRIC")
public class EGCompleteBiometric extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "EG_COMPLETE_BIOMETRIC_id_gen")
    @SequenceGenerator(name = "EG_COMPLETE_BIOMETRIC_id_gen", sequenceName = "EG_COMPLETE_BIOMETRIC_ID_SEQ", allocationSize = 1)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Size(max = 40)
    @NotNull
    @Nationalized
    @Column(name = "CUSTOMER_NAME", nullable = false, length = 40)
    private String customerName;

    @Size(max = 20)
    @NotNull
    @Column(name = "CIF_BANK", nullable = false, length = 20)
    private String cifBank;

    @Size(max = 20)
    @NotNull
    @Nationalized
    @Column(name = "PHONE", nullable = false, length = 20)
    private String phone;

    @Size(max = 100)
    @Column(name = "UNIQUE_TYPE", length = 100)
    private String uniqueType;

    @Size(max = 100)
    @Column(name = "UNIQUE_VALUE", length = 100)
    private String uniqueValue;

    @NotNull
    @Column(name = "COMPLETE_AT", nullable = false)
    private Date completeAt;

    @Column(name = "IS_PLUS_POINT")
    private boolean isPlusPoint;

}