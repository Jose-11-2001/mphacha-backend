package mw.mphachayanga.comment;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mw.mphachayanga.comment.dto.*;
import mw.mphachayanga.user.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/product/{productId}")
    public List<CommentResponse> byProduct(@PathVariable Long productId) {
        return commentService.byProduct(productId);
    }

    @PostMapping
    public CommentResponse create(@AuthenticationPrincipal User user,
                                  @Valid @RequestBody CommentRequest req) {
        return commentService.create(user, req);
    }
}