package mw.mphachayanga.delivery;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "delivery_fees")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class DeliveryFee {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String region;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal fee;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;
}