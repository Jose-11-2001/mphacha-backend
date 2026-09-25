package mw.mphachayanga.order.dto;

import mw.mphachayanga.order.Order;
import mw.mphachayanga.order.OrderItem;
import mw.mphachayanga.order.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderResponse(
        Long id,
        Long customerId,
        String customerName,
        BigDecimal totalProductPrice,
        BigDecimal deliveryFee,
        Boolean requiresDelivery,
        BigDecimal totalAmount,
        String deliveryAddress,
        String region,
        OrderStatus status,
        LocalDateTime createdAt,
        List<Item> items
) {
    public record Item(Long productId, String productName, Integer quantity, BigDecimal priceAtPurchase) {
        public static Item from(OrderItem i) {
            return new Item(i.getProductId(), i.getProductName(), i.getQuantity(), i.getPriceAtPurchase());
        }
    }

    public static OrderResponse from(Order o) {
        return new OrderResponse(
                o.getId(),
                o.getCustomer().getId(),
                o.getCustomer().getFullName(),
                o.getTotalProductPrice(),
                o.getDeliveryFee(),
                o.getRequiresDelivery(),
                o.getTotalAmount(),
                o.getDeliveryAddress(),
                o.getRegion(),
                o.getStatus(),
                o.getCreatedAt(),
                o.getItems().stream().map(Item::from).toList()
        );
    }
}