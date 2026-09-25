package mw.mphachayanga.comment;

import lombok.RequiredArgsConstructor;
import mw.mphachayanga.comment.dto.*;
import mw.mphachayanga.common.exception.ResourceNotFoundException;
import mw.mphachayanga.product.Product;
import mw.mphachayanga.product.ProductRepository;
import mw.mphachayanga.user.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository repo;
    private final ProductRepository productRepo;

    public List<CommentResponse> byProduct(Long productId) {
        return repo.findByProductIdOrderByCreatedAtDesc(productId)
                .stream().map(CommentResponse::from).toList();
    }

    public List<CommentResponse> all() {
        return repo.findAllByOrderByCreatedAtDesc().stream().map(CommentResponse::from).toList();
    }

    public CommentResponse create(User customer, CommentRequest req) {
        Product p = productRepo.findById(req.productId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        Comment c = Comment.builder()
                .product(p)
                .customer(customer)
                .text(req.text())
                .build();
        return CommentResponse.from(repo.save(c));
    }

    public void delete(Long id) {
        if (!repo.existsById(id)) throw new ResourceNotFoundException("Comment not found");
        repo.deleteById(id);
    }
}