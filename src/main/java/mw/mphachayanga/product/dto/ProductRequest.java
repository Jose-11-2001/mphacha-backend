package mw.mphachayanga.product.dto;

import jakarta.validation.constraints.*;
import mw.mphachayanga.product.Category;

import java.math.BigDecimal;

public record ProductRequest(
        @NotBlank String name,
        String description,
        @NotNull @DecimalMin("0.0") BigDecimal price,
        @NotNull @Min(0) Integer stockQuantity,
        @NotNull Category category,
        Boolean isActive
) {}