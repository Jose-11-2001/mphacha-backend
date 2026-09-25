package mw.mphachayanga.product;

import lombok.RequiredArgsConstructor;
import mw.mphachayanga.product.dto.*;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/admin/products")
@RequiredArgsConstructor
public class AdminProductController {

    private final ProductService productService;

    @GetMapping
    public List<ProductResponse> listAll() {
        return productService.listAll();
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ProductResponse create(
            @RequestPart("data") ProductRequest data,
            @RequestPart(value = "image", required = false) MultipartFile image) {
        return productService.create(data, image);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ProductResponse update(
            @PathVariable Long id,
            @RequestPart("data") ProductRequest data,
            @RequestPart(value = "image", required = false) MultipartFile image) {
        return productService.update(id, data, image);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        productService.delete(id);
    }
}