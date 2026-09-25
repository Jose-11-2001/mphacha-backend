package mw.mphachayanga.order;

import lombok.RequiredArgsConstructor;
import mw.mphachayanga.order.dto.OrderResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/orders")
@RequiredArgsConstructor
public class AdminOrderController {

    private final OrderService orderService;

    @GetMapping
    public List<OrderResponse> all() {
        return orderService.all();
    }

    @PatchMapping("/{id}/status")
    public OrderResponse updateStatus(@PathVariable Long id, @RequestBody Map<String, String> body) {
        return orderService.updateStatus(id, OrderStatus.valueOf(body.get("status")));
    }
}