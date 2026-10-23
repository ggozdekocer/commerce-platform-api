package tr.com.java.commerceplatform.product.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import tr.com.java.commerceplatform.common.response.CommonResponse;
import tr.com.java.commerceplatform.product.dto.ProductListResponse;
import tr.com.java.commerceplatform.product.entity.Product;
import tr.com.java.commerceplatform.product.service.ProductService;

import java.util.List;

/** REST endpoints for reading products. */
@RestController
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    /**
     * Returns all products together with the total count.
     *
     * @return HTTP 200 with the product list wrapped in a CommonResponse
     */
    @GetMapping("/products")
    public ResponseEntity<CommonResponse<ProductListResponse>> getProducts() {
        List<Product> products = productService.getAll();
        ProductListResponse payload = new ProductListResponse(products, products.size());
        return ResponseEntity.ok(CommonResponse.success(payload, "Products retrieved successfully"));
    }

    /**
     * Returns a single product by its id.
     *
     * @param id the id of the product
     * @return HTTP 200 with the product wrapped in a CommonResponse
     */
    @GetMapping("/products/{id}")
    public ResponseEntity<CommonResponse<Product>> getProductById(@PathVariable Long id) {
        Product product = productService.getById(id);
        return ResponseEntity.ok(CommonResponse.success(product, "Product retrieved successfully"));
    }
}