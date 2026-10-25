package mw.mphachayanga.payment;

import lombok.RequiredArgsConstructor;
import mw.mphachayanga.order.Order;
import mw.mphachayanga.order.OrderRepository;
import mw.mphachayanga.order.OrderStatus;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;
    private final OrderRepository orderRepo;

    @PostMapping("/webhook")
    public void handleWebhook(@RequestBody Map<String, Object> payload) {
        String txRef = (String) payload.get("tx_ref");
        
        // Find the order by txRef
        Order order = orderRepo.findByPaymentReference(txRef)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        
        // Verify payment with PayChangu
        if (paymentService.verifyPayment(txRef)) {
            order.setStatus(OrderStatus.CONFIRMED); // Update order status
            orderRepo.save(order);
        }
    }
}