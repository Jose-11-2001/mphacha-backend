package mw.mphachayanga.order.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record OrderRequest(
        @NotEmpty List<OrderItemRequest> items,
        @NotNull Boolean requiresDelivery,
        String deliveryAddress,
        String region
) {}