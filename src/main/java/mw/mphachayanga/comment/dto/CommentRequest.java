package mw.mphachayanga.comment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CommentRequest(
        @NotNull Long productId,
        @NotBlank @Size(max = 1000) String text
) {}