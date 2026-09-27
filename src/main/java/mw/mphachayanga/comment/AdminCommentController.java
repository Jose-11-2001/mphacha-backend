package mw.mphachayanga.comment;

import lombok.RequiredArgsConstructor;
import mw.mphachayanga.comment.dto.CommentResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;
//hfd
@RestController
@RequestMapping("/api/admin/comments")
@RequiredArgsConstructor
public class AdminCommentController {

    private final CommentService commentService;

    @GetMapping
    public List<CommentResponse> all() {
        return commentService.all();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        commentService.delete(id);
    }
}