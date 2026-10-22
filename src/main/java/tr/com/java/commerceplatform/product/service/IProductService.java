package tr.com.java.commerceplatform.product.service;

import tr.com.java.commerceplatform.product.entity.Product;
import java.util.List;

public interface IProductService {
    List<Product> getAll();
    Product getById(Long id);
}