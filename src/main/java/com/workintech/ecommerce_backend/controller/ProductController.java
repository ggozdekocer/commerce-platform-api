package com.workintech.ecommerce_backend.controller;

import com.workintech.ecommerce_backend.entity.Product;
import com.workintech.ecommerce_backend.service.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class ProductController {

    private final IProductService productService;


    @GetMapping("/products")
    public Map<String, Object> getProducts() {
        List<Product> products = productService.getAll();
        return Map.of(
                "products", products,
                "total", (long) products.size()
        );
    }
    @GetMapping("/products/{id}")
    public Product getProductById(@PathVariable Long id) {
        return productService.getById(id);
    }


}