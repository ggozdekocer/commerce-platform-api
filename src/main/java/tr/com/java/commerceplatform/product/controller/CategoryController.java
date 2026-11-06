package tr.com.java.commerceplatform.product.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import tr.com.java.commerceplatform.common.response.CommonResponse;
import tr.com.java.commerceplatform.product.entity.Category;
import tr.com.java.commerceplatform.product.service.CategoryService;

import java.util.List;

/** REST endpoints for reading product categories. */
@RestController
@RequiredArgsConstructor
@Tag(name = "Categories", description = "Browse product categories")
public class CategoryController {

    private final CategoryService categoryService;

    /**
     * Returns all product categories.
     *
     * @return HTTP 200 with the category list wrapped in a CommonResponse
     */
    @Operation(
            summary = "List all categories",
            description = "Returns every product category. No login required.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Categories returned")
    })
    @GetMapping("/categories")
    public ResponseEntity<CommonResponse<List<Category>>> getCategories() {
        List<Category> categories = categoryService.getAll();
        return ResponseEntity.ok(CommonResponse.success(categories, "Categories retrieved successfully"));
    }
}