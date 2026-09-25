package mw.mphachayanga.comment.dto;

import mw.mphachayanga.comment.Comment;

import java.time.LocalDateTime;

public record CommentResponse(
        Long id, Long productId, String productName,
        Long customerId, String customerName,
        String text, LocalDateTime createdAt
) {
    public static CommentResponse from(Comment c) {
        return new CommentResponse(
                c.getId(),
                c.getProduct() != null ? c.getProduct().getId() : null,
                c.getProduct() != null ? c.getProduct().getName() : null,
                c.getCustomer().getId(),
                c.getCustomer().getFullName(),
                c.getText(),
                c.getCreatedAt()
        );
    }
}