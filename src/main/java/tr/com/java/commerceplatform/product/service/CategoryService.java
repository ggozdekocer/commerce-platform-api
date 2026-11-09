package tr.com.java.commerceplatform.product.service;

import tr.com.java.commerceplatform.product.entity.Category;

import java.util.List;

/** Business operations for reading product categories. */
public interface CategoryService {

    /**
     * Returns all product categories.
     *
     * @return the list of all categories, empty when there are none
     */
    List<Category> getAll();
}