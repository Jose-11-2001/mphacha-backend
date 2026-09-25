package mw.mphachayanga.product.dto;

import mw.mphachayanga.product.Category;
import mw.mphachayanga.product.Product;

import java.math.BigDecimal;

public record ProductResponse(
        Long id, String name, String description, BigDecimal price,
        Integer stockQuantity, Category category, String imageUrl, Boolean isActive
) {
    public static ProductResponse from(Product p) {
        return new ProductResponse(p.getId(), p.getName(), p.getDescription(),
                p.getPrice(), p.getStockQuantity(), p.getCategory(),
                p.getImageUrl(), p.getIsActive());
    }
}