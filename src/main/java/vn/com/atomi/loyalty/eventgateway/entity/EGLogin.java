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
@Table(name = "EG_LOGIN")
public class EGLogin extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "EG_LOGIN_id_gen")
    @SequenceGenerator(name = "EG_LOGIN_id_gen", sequenceName = "EG_LOGIN_ID_SEQ", allocationSize = 1)
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

    @NotNull
    @Column(name = "LOGIN_AT", nullable = false)
    private Date loginAt;
}
