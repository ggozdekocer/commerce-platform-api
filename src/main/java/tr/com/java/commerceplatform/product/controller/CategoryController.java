package tr.com.java.commerceplatform.product.controller;

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
public class CategoryController {

    private final CategoryService categoryService;

    /**
     * Returns all product categories.
     *
     * @return HTTP 200 with the category list wrapped in a CommonResponse
     */
    @GetMapping("/categories")
    public ResponseEntity<CommonResponse<List<Category>>> getCategories() {
        List<Category> categories = categoryService.getAll();
        return ResponseEntity.ok(CommonResponse.success(categories, "Categories retrieved successfully"));
    }
}