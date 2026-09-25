package mw.mphachayanga.delivery;

import lombok.RequiredArgsConstructor;
import mw.mphachayanga.common.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DeliveryFeeService {

    private final DeliveryFeeRepository repo;

    public List<DeliveryFee> listActive() {
        return repo.findAll().stream().filter(DeliveryFee::getIsActive).toList();
    }

    public BigDecimal feeFor(String region) {
        return repo.findByRegionAndIsActiveTrue(region)
                .map(DeliveryFee::getFee)
                .orElseThrow(() -> new ResourceNotFoundException("No delivery fee for region: " + region));
    }

    public DeliveryFee upsert(String region, BigDecimal fee) {
        DeliveryFee df = repo.findByRegionAndIsActiveTrue(region)
                .orElseGet(() -> DeliveryFee.builder().region(region).build());
        df.setFee(fee);
        df.setIsActive(true);
        return repo.save(df);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }
}