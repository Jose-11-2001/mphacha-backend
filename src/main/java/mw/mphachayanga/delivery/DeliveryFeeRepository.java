package mw.mphachayanga.delivery;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface DeliveryFeeRepository extends JpaRepository<DeliveryFee, Long> {
    Optional<DeliveryFee> findByRegionAndIsActiveTrue(String region);
}