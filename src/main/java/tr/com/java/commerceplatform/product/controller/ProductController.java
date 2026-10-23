package tr.com.java.commerceplatform.product.controller;

import tr.com.java.commerceplatform.product.entity.Product;
import tr.com.java.commerceplatform.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;


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