package tr.com.java.commerceplatform.product.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tr.com.java.commerceplatform.common.response.CommonResponse;
import tr.com.java.commerceplatform.product.dto.ProductListResponse;
import tr.com.java.commerceplatform.product.entity.Product;
import tr.com.java.commerceplatform.product.service.ProductService;

/** REST endpoints for reading products. */
@RestController
@RequiredArgsConstructor
@Tag(name = "Products", description = "Browse the product catalog")
public class ProductController {

    private final ProductService productService;

    /**
     * Returns one page of products, optionally filtered and sorted, together with the total count.
     *
     * @param category only products of this category id
     * @param filter   text that must appear in the product name or description
     * @param sort     sort order as field:direction, for example price:desc
     * @param offset   number of products to skip
     * @param limit    number of products per page
     * @return HTTP 200 with the product page wrapped in a CommonResponse
     */
    @Operation(
            summary = "List products",
            description = "Returns a page of products with their category and images. The total is the number of "
                    + "products that match the filters, not only the ones on this page. No login required.")
    @ApiResponse(responseCode = "200", description = "Products returned")
    @ApiResponse(responseCode = "400", description = "The offset, limit or sort field is not valid")
    @GetMapping("/products")
    public ResponseEntity<CommonResponse<ProductListResponse>> getProducts(
            @Parameter(description = "Category id") @RequestParam(required = false) Long category,
            @Parameter(description = "Text to look for in the name or description")
            @RequestParam(required = false) String filter,
            @Parameter(description = "Sort as field:direction. Fields: price, rating, sell_count. Example: price:desc")
            @RequestParam(required = false) String sort,
            @Parameter(description = "Number of products to skip") @RequestParam(defaultValue = "0") int offset,
            @Parameter(description = "Products per page, 1 to 100") @RequestParam(defaultValue = "25") int limit) {
        Page<Product> page = productService.search(category, filter, sort, offset, limit);
        ProductListResponse payload = new ProductListResponse(page.getContent(), page.getTotalElements());
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