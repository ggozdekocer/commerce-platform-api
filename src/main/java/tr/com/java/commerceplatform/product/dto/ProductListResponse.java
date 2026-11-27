package tr.com.java.commerceplatform.product.dto;

import tr.com.java.commerceplatform.product.entity.Product;

import java.util.List;

/**
 * Payload of the product list endpoint.
 *
 * @param products the products to return
 * @param total    the number of products in the list
 */
public record ProductListResponse(
        List<Product> products,
        long total
) {
}