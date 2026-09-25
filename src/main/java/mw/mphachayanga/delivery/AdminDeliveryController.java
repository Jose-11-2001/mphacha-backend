package mw.mphachayanga.delivery;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AdminDeliveryController {

    private final DeliveryFeeService service;

    /** Public — customers need this at checkout to see fee per region */
    @GetMapping("/delivery-fees/public")
    public List<DeliveryFee> publicList() {
        return service.listActive();
    }

    /** Admin CRUD */
    @GetMapping("/admin/delivery-fees")
    public List<DeliveryFee> adminList() {
        return service.listActive();
    }

    @PostMapping("/admin/delivery-fees")
    public DeliveryFee upsert(@RequestBody Map<String, Object> body) {
        String region = (String) body.get("region");
        BigDecimal fee = new BigDecimal(body.get("fee").toString());
        return service.upsert(region, fee);
    }

    @DeleteMapping("/admin/delivery-fees/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}