package mw.mphachayanga.order;

import lombok.RequiredArgsConstructor;
import mw.mphachayanga.common.exception.ApiException;
import mw.mphachayanga.common.exception.ResourceNotFoundException;
import mw.mphachayanga.delivery.DeliveryFeeService;
import mw.mphachayanga.order.dto.*;
import mw.mphachayanga.product.Product;
import mw.mphachayanga.product.ProductRepository;
import mw.mphachayanga.user.User;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepo;
    private final ProductRepository productRepo;
    private final DeliveryFeeService deliveryFeeService;

    @Transactional
    public OrderResponse create(User customer, OrderRequest req) {
        Order order = Order.builder()
                .customer(customer)
                .requiresDelivery(req.requiresDelivery())
                .deliveryAddress(req.deliveryAddress())
                .region(req.region())
                .status(OrderStatus.PENDING)
                .build();

        BigDecimal total = BigDecimal.ZERO;

        for (OrderItemRequest itemReq : req.items()) {
            Product product = productRepo.findById(itemReq.productId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + itemReq.productId()));

            if (!product.getIsActive()) {
                throw new ApiException("Product unavailable: " + product.getName(), HttpStatus.BAD_REQUEST);
            }
            if (product.getStockQuantity() < itemReq.quantity()) {
                throw new ApiException("Insufficient stock for: " + product.getName(), HttpStatus.BAD_REQUEST);
            }

            product.setStockQuantity(product.getStockQuantity() - itemReq.quantity());
            productRepo.save(product);

            BigDecimal lineTotal = product.getPrice().multiply(BigDecimal.valueOf(itemReq.quantity()));
            total = total.add(lineTotal);

            order.addItem(OrderItem.builder()
                    .productId(product.getId())
                    .productName(product.getName())
                    .quantity(itemReq.quantity())
                    .priceAtPurchase(product.getPrice())
                    .build());
        }

        order.setTotalProductPrice(total);

        BigDecimal deliveryFee = BigDecimal.ZERO;
        if (Boolean.TRUE.equals(req.requiresDelivery())) {
            if (req.region() == null || req.region().isBlank()) {
                throw new ApiException("Region is required when delivery is selected", HttpStatus.BAD_REQUEST);
            }
            deliveryFee = deliveryFeeService.feeFor(req.region());
        }
        order.setDeliveryFee(deliveryFee);
        order.setTotalAmount(total.add(deliveryFee));

        return OrderResponse.from(orderRepo.save(order));
    }

    public List<OrderResponse> myOrders(Long customerId) {
        return orderRepo.findByCustomerIdOrderByCreatedAtDesc(customerId)
                .stream().map(OrderResponse::from).toList();
    }

    public OrderResponse getOne(Long id, Long customerId) {
        Order o = orderRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        if (!o.getCustomer().getId().equals(customerId)) {
            throw new ApiException("Not your order", HttpStatus.FORBIDDEN);
        }
        return OrderResponse.from(o);
    }

    public List<OrderResponse> all() {
        return orderRepo.findAllByOrderByCreatedAtDesc().stream().map(OrderResponse::from).toList();
    }

    @Transactional
    public OrderResponse updateStatus(Long id, OrderStatus status) {
        Order o = orderRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        o.setStatus(status);
        return OrderResponse.from(orderRepo.save(o));
    }
}