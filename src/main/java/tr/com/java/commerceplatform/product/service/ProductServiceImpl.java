package tr.com.java.commerceplatform.product.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import tr.com.java.commerceplatform.common.exception.BadRequestException;
import tr.com.java.commerceplatform.common.exception.ResourceNotFoundException;
import tr.com.java.commerceplatform.product.entity.Product;
import tr.com.java.commerceplatform.product.repository.ProductRepository;

import java.util.List;
import java.util.Map;

/** Default implementation of {@link ProductService} backed by the product repository. */
@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private static final int MAX_LIMIT = 100;

    /** Sort names a client may use, mapped to the Product field they sort by. */
    private static final Map<String, String> SORT_FIELDS = Map.of(
            "price", "price",
            "rating", "rating",
            "sell_count", "sellCount",
            "sellCount", "sellCount");

    private final ProductRepository productRepository;

    /** {@inheritDoc} */
    @Override
    public List<Product> getAll() {
        log.debug("Loading all products");
        return productRepository.findAll();
    }

    /**
     * {@inheritDoc}
     *
     * <p>The filters are optional and combine with AND. Products with the same sort value are
     * ordered by id, so the pages always come back in a stable order.
     */
    @Override
    public Page<Product> search(Long categoryId, String filter, String sort, int offset, int limit) {
        if (offset < 0) {
            throw new BadRequestException("Offset must not be negative");
        }
        if (limit < 1 || limit > MAX_LIMIT) {
            throw new BadRequestException("Limit must be between 1 and " + MAX_LIMIT);
        }

        log.debug("Searching products: category={}, filter='{}', sort={}, offset={}, limit={}",
                categoryId, filter, sort, offset, limit);

        Pageable pageable = PageRequest.of(offset / limit, limit, parseSort(sort));
        Specification<Product> specification = Specification
                .where(hasCategory(categoryId))
                .and(nameOrDescriptionContains(filter));

        return productRepository.findAll(specification, pageable);
    }

    /**
     * {@inheritDoc}
     *
     * @throws ResourceNotFoundException if no product exists with the given id
     */
    @Override
    public Product getById(Long id) {
        log.debug("Loading product id={}", id);
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
    }

    /** Matches products of one category, or every product when no category is given. */
    private Specification<Product> hasCategory(Long categoryId) {
        if (categoryId == null) {
            return null;
        }
        return (root, query, builder) -> builder.equal(root.get("category").get("id"), categoryId);
    }

    /** Matches products whose name or description contains the text, ignoring upper and lower case. */
    private Specification<Product> nameOrDescriptionContains(String filter) {
        if (filter == null || filter.isBlank()) {
            return null;
        }
        String pattern = "%" + filter.trim().toLowerCase() + "%";
        return (root, query, builder) -> builder.or(
                builder.like(builder.lower(root.get("name")), pattern),
                builder.like(builder.lower(root.get("description")), pattern));
    }

    /** Turns a value such as {@code price:desc} into a Sort, and rejects fields that are not allowed. */
    private Sort parseSort(String sort) {
        if (sort == null || sort.isBlank()) {
            return Sort.by("id");
        }
        String[] parts = sort.split(":");
        String field = SORT_FIELDS.get(parts[0].trim());
        if (field == null) {
            throw new BadRequestException("Products cannot be sorted by: " + parts[0].trim());
        }
        boolean descending = parts.length > 1 && "desc".equalsIgnoreCase(parts[1].trim());
        Sort.Direction direction = descending ? Sort.Direction.DESC : Sort.Direction.ASC;
        return Sort.by(direction, field).and(Sort.by("id"));
    }
}