package tr.com.java.commerceplatform.product.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tr.com.java.commerceplatform.common.exception.ResourceNotFoundException;
import tr.com.java.commerceplatform.product.entity.Product;
import tr.com.java.commerceplatform.product.repository.ProductRepository;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import tr.com.java.commerceplatform.common.exception.BadRequestException;

import static org.mockito.ArgumentMatchers.any;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/** Unit tests for {@link ProductServiceImpl}. */
@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    @Test
    @DisplayName("Returns every product found by the repository")
    void shouldReturnAllProducts() {
        Product first = new Product();
        first.setId(1L);
        Product second = new Product();
        second.setId(2L);
        when(productRepository.findAll()).thenReturn(List.of(first, second));

        List<Product> result = productService.getAll();

        assertEquals(2, result.size());
        verify(productRepository).findAll();
    }

    @Test
    @DisplayName("Returns the product when the id exists")
    void shouldReturnProductById() {
        Product product = new Product();
        product.setId(5L);
        when(productRepository.findById(5L)).thenReturn(Optional.of(product));

        Product result = productService.getById(5L);

        assertSame(product, result);
    }

    @Test
    @DisplayName("Throws ResourceNotFoundException when no product has the given id")
    void shouldFailWhenProductIsMissing() {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception =
                assertThrows(ResourceNotFoundException.class, () -> productService.getById(99L));

        assertEquals("Product not found with id: 99", exception.getMessage());
    }

    @Test
    @DisplayName("Searches with the page, page size and sort order that were asked for")
    void shouldSearchWithPagingAndSorting() {
        when(productRepository.findAll(ArgumentMatchers.<Specification<Product>>any(), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(new Product())));

        productService.search(1L, "shirt", "price:desc", 50, 25);

        ArgumentCaptor<Pageable> captor = ArgumentCaptor.forClass(Pageable.class);
        verify(productRepository).findAll(ArgumentMatchers.<Specification<Product>>any(), captor.capture());
        Pageable pageable = captor.getValue();
        assertEquals(2, pageable.getPageNumber());
        assertEquals(25, pageable.getPageSize());
        assertEquals(Sort.Direction.DESC, pageable.getSort().getOrderFor("price").getDirection());
    }

    @Test
    @DisplayName("Throws BadRequestException when the limit is larger than 100")
    void shouldFailSearchWhenLimitIsTooLarge() {
        assertThrows(BadRequestException.class, () -> productService.search(null, null, null, 0, 101));
    }

    @Test
    @DisplayName("Throws BadRequestException when the offset is negative")
    void shouldFailSearchWhenOffsetIsNegative() {
        assertThrows(BadRequestException.class, () -> productService.search(null, null, null, -1, 25));
    }

    @Test
    @DisplayName("Throws BadRequestException when the sort field is not allowed")
    void shouldFailSearchWhenSortFieldIsUnknown() {
        assertThrows(BadRequestException.class, () -> productService.search(null, null, "color:asc", 0, 25));
    }
}