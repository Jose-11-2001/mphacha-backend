package mw.mphachayanga.product;

import lombok.RequiredArgsConstructor;
import mw.mphachayanga.common.exception.ResourceNotFoundException;
import mw.mphachayanga.common.util.FileUploadUtil;
import mw.mphachayanga.product.dto.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository repo;
    private final FileUploadUtil uploadUtil;

    public List<ProductResponse> listPublic(Category category) {
        List<Product> products = category == null
                ? repo.findByIsActiveTrueOrderByCreatedAtDesc()
                : repo.findByCategoryAndIsActiveTrueOrderByCreatedAtDesc(category);
        return products.stream().map(ProductResponse::from).toList();
    }

    public ProductResponse get(Long id) {
        return ProductResponse.from(repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found")));
    }

    public List<ProductResponse> listAll() {
        return repo.findAll().stream().map(ProductResponse::from).toList();
    }

    public ProductResponse create(ProductRequest req, MultipartFile image) {
        Product p = Product.builder()
                .name(req.name())
                .description(req.description())
                .price(req.price())
                .stockQuantity(req.stockQuantity())
                .category(req.category())
                .isActive(req.isActive() == null || req.isActive())
                .build();
        if (image != null && !image.isEmpty()) {
            p.setImageUrl(uploadUtil.uploadImage(image));
        }
        return ProductResponse.from(repo.save(p));
    }

    public ProductResponse update(Long id, ProductRequest req, MultipartFile image) {
        Product p = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        p.setName(req.name());
        p.setDescription(req.description());
        p.setPrice(req.price());
        p.setStockQuantity(req.stockQuantity());
        p.setCategory(req.category());
        if (req.isActive() != null) p.setIsActive(req.isActive());
        if (image != null && !image.isEmpty()) {
            p.setImageUrl(uploadUtil.uploadImage(image));
        }
        return ProductResponse.from(repo.save(p));
    }

    public void delete(Long id) {
        if (!repo.existsById(id)) throw new ResourceNotFoundException("Product not found");
        repo.deleteById(id);
    }
}