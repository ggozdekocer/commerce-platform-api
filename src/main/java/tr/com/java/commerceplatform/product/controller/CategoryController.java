package tr.com.java.commerceplatform.product.controller;

import tr.com.java.commerceplatform.product.entity.Category;
import tr.com.java.commerceplatform.product.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/categories")
    public List<Category> getCategories() {
        return categoryService.getAll();
    }
}