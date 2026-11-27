package tr.com.java.commerceplatform.product.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tr.com.java.commerceplatform.product.entity.Category;
import tr.com.java.commerceplatform.product.repository.CategoryRepository;

import java.util.List;

/** Default implementation of {@link CategoryService} backed by the category repository. */
@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    /** {@inheritDoc} */
    @Override
    public List<Category> getAll() {
        log.debug("Loading all categories");
        return categoryRepository.findAll();
    }
}