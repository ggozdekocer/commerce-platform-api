package tr.com.java.commerceplatform.product.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Products", description = "Browse the product catalog")
public class ProductController {

    private final ProductService productService;

    /**
     * Returns all products together with the total count.
     *
     * @return HTTP 200 with the product list wrapped in a CommonResponse
     */
    @Operation(
            summary = "List all products",
            description = "Returns every product with its category and images, plus the total number of products. "
                    + "No login required.")
    @ApiResponse(responseCode = "200", description = "Products returned")
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
    @Operation(
            summary = "Get a product by id",
            description = "Returns one product with its category and images. No login required.")
    @ApiResponse(responseCode = "200", description = "Product returned")
    @ApiResponse(responseCode = "404", description = "No product exists with the given id")
    @GetMapping("/products/{id}")
    public ResponseEntity<CommonResponse<Product>> getProductById(@PathVariable Long id) {
        Product product = productService.getById(id);
        return ResponseEntity.ok(CommonResponse.success(product, "Product retrieved successfully"));
    }
}