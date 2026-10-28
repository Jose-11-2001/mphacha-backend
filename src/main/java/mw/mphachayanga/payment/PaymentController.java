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

    // 1. Endpoint to start the payment (called by frontend)
    @PostMapping("/initiate")
    public Map<String, String> initiate(@RequestBody Map<String, Long> body) {
        Order order = orderRepo.findById(body.get("orderId"))
                .orElseThrow(() -> new RuntimeException("Order not found"));
        String url = paymentService.initiatePayment(order);
        return Map.of("checkoutUrl", url);
    }

    // 2. Webhook (Accepts POST from PayChangu, GET from browser for testing)
    @RequestMapping(value = "/webhook", method = {RequestMethod.GET, RequestMethod.POST})
    public void handleWebhook(@RequestBody(required = false) Map<String, Object> payload) {
        // If it's a GET request from a browser, payload will be null. Just return 200 OK.
        if (payload == null) {
            return; 
        }
        
        String txRef = (String) payload.get("tx_ref");
        
        if (txRef != null) {
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
}