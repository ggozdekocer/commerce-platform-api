package tr.com.java.commerceplatform.product.service;

import tr.com.java.commerceplatform.common.exception.ResourceNotFoundException;
import tr.com.java.commerceplatform.product.entity.Product;

import java.util.List;

/** Business operations for reading products. */
public interface ProductService {

    /**
     * Returns all products.
     *
     * @return the list of all products, empty when there are none
     */
    List<Product> getAll();

    /**
     * Returns the product with the given id.
     *
     * @param id the id of the product
     * @return the matching product
     * @throws ResourceNotFoundException if no product exists with the given id
     */
    Product getById(Long id);
}