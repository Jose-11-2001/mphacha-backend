package mw.mphachayanga.product;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByIsActiveTrueOrderByCreatedAtDesc();
    List<Product> findByCategoryAndIsActiveTrueOrderByCreatedAtDesc(Category category);
}