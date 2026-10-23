package tr.com.java.commerceplatform.product.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tr.com.java.commerceplatform.common.exception.ResourceNotFoundException;
import tr.com.java.commerceplatform.product.entity.Product;
import tr.com.java.commerceplatform.product.repository.ProductRepository;

import java.util.List;

/** Default implementation of {@link ProductService} backed by the product repository. */
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    /** {@inheritDoc} */
    @Override
    public List<Product> getAll() {
        return productRepository.findAll();
    }

    /**
     * {@inheritDoc}
     *
     * @throws ResourceNotFoundException if no product exists with the given id
     */
    @Override
    public Product getById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
    }
}