package tr.com.java.commerceplatform.product.service;

import org.springframework.data.domain.Page;
import tr.com.java.commerceplatform.common.exception.BadRequestException;
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
     * Searches the products with optional filters, sorting and paging.
     *
     * @param categoryId only products of this category, or null for every category
     * @param filter     text that must appear in the product name or description, or null for no text filter
     * @param sort       sort order as {@code field:direction}, for example {@code price:desc}, or null
     * @param offset     number of products to skip, used to find the page (offset / limit)
     * @param limit      number of products per page, between 1 and 100
     * @return the requested page together with the total number of matching products
     * @throws BadRequestException if the offset, limit or sort field is not valid
     */
    Page<Product> search(Long categoryId, String filter, String sort, int offset, int limit);

    /**
     * Returns the product with the given id.
     *
     * @param id the id of the product
     * @return the matching product
     * @throws ResourceNotFoundException if no product exists with the given id
     */
    Product getById(Long id);
}