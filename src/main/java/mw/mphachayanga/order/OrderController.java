package mw.mphachayanga.order;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mw.mphachayanga.order.dto.*;
import mw.mphachayanga.user.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public OrderResponse create(@AuthenticationPrincipal User user,
                                @Valid @RequestBody OrderRequest req) {
        return orderService.create(user, req);
    }

    @GetMapping
    public List<OrderResponse> myOrders(@AuthenticationPrincipal User user) {
        return orderService.myOrders(user.getId());
    }

    @GetMapping("/{id}")
    public OrderResponse getOne(@AuthenticationPrincipal User user, @PathVariable Long id) {
        return orderService.getOne(id, user.getId());
    }
}