package tr.com.java.commerceplatform.product.repository;

import tr.com.java.commerceplatform.product.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
