package mw.mphachayanga.payment;

import lombok.RequiredArgsConstructor;
import mw.mphachayanga.order.Order;
import mw.mphachayanga.order.OrderRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import java.math.BigDecimal;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final OrderRepository orderRepo;
    private final WebClient.Builder webClientBuilder;

    @Value("${paychangu.secret-key}")
    private String secretKey;
    @Value("${paychangu.callback-url}")
    private String callbackUrl;
    @Value("${paychangu.return-url}")
    private String returnUrl;

    public String initiatePayment(Order order) {
        // Generate a unique reference for this order
        String txRef = "MPHACHA_ORDER_" + order.getId() + "_" + System.currentTimeMillis();
        
        // Save txRef to the order so we can verify it later
        order.setPaymentReference(txRef);
        orderRepo.save(order);

        Map<String, Object> body = Map.of(
            "amount", order.getTotalAmount().toString(),
            "currency", "MWK",
            "email", "customer@example.com", // Ideally, get this from the User entity
            "first_name", "Customer",
            "last_name", "Name",
            "callback_url", callbackUrl,
            "return_url", returnUrl,
            "tx_ref", txRef,
            "customization", Map.of(
                "title", "Mphacha-Yanga Order #" + order.getId(),
                "description", "Payment for clothing order"
            )
        );

        // Call PayChangu
        Map response = webClientBuilder.build()
            .post()
            .uri("https://api.paychangu.com/payment")
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + secretKey)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(body)
            .retrieve()
            .bodyToMono(Map.class)
            .block();

        // Return the checkout URL to the frontend
        Map data = (Map) response.get("data");
        return (String) data.get("checkout_url");
    }

    public boolean verifyPayment(String txRef) {
        Map response = webClientBuilder.build()
            .get()
            .uri("https://api.paychangu.com/verify-payment/" + txRef)
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + secretKey)
            .retrieve()
            .bodyToMono(Map.class)
            .block();

        // Check if status is success
        return "success".equals(response.get("status"));
    }
}