package mw.mphachayanga.product;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mw.mphachayanga.common.exception.ResourceNotFoundException;
import mw.mphachayanga.common.util.FileUploadUtil;
import mw.mphachayanga.product.dto.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
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
        p.setImageUrl(safeUpload(image));
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

        String newImageUrl = safeUpload(image);
        if (newImageUrl != null) {
            p.setImageUrl(newImageUrl);
        }
        return ProductResponse.from(repo.save(p));
    }

    public void delete(Long id) {
        if (!repo.existsById(id)) throw new ResourceNotFoundException("Product not found");
        repo.deleteById(id);
    }

    /**
     * Attempts to upload the image. If Cloudinary isn't configured or the upload
     * fails, logs the error and returns null so the product still saves without
     * an image. This keeps product creation resilient when Cloudinary env vars
     * are missing (e.g. during early testing).
     */
    private String safeUpload(MultipartFile image) {
        if (image == null || image.isEmpty()) {
            return null;
        }
        try {
            return uploadUtil.uploadImage(image);
        } catch (Exception e) {
            log.warn("Image upload failed, saving product without image: {}", e.getMessage());
            return null;
        }
    }
}