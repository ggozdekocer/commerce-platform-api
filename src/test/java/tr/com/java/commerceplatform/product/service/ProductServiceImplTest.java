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
}