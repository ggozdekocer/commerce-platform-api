package tr.com.java.commerceplatform.order.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tr.com.java.commerceplatform.common.exception.BadRequestException;
import tr.com.java.commerceplatform.common.exception.InsufficientStockException;
import tr.com.java.commerceplatform.common.exception.ResourceNotFoundException;
import tr.com.java.commerceplatform.order.dto.OrderRequest;
import tr.com.java.commerceplatform.order.entity.Order;
import tr.com.java.commerceplatform.order.repository.OrderRepository;
import tr.com.java.commerceplatform.product.entity.Product;
import tr.com.java.commerceplatform.product.repository.ProductRepository;
import tr.com.java.commerceplatform.user.entity.User;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/** Unit tests for the order workflow in {@link OrderServiceImpl}. */
@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private OrderServiceImpl orderService;

    private User buildUser() {
        User user = new User();
        user.setId(1L);
        return user;
    }

    private Product buildProduct(Long id, double price, int stock, int sellCount) {
        Product product = new Product();
        product.setId(id);
        product.setPrice(price);
        product.setStock(stock);
        product.setSellCount(sellCount);
        return product;
    }

    private OrderRequest buildRequest(Double price, List<OrderRequest.OrderProductRequest> lines) {
        return new OrderRequest(1L, "2026-11-11", "4111111111111111", "Test User", 12, 2030, 123, price, lines);
    }

    @Test
    @DisplayName("Creates the order, reduces stock, raises sell count and calculates the price on the server")
    void shouldCreateOrderAndUpdateStock() {
        Product product = buildProduct(10L, 100.0, 10, 5);
        when(productRepository.findById(10L)).thenReturn(Optional.of(product));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // the client sends a wrong price (1.0), the server must ignore it
        OrderRequest request = buildRequest(1.0, List.of(new OrderRequest.OrderProductRequest(10L, 3, "M")));

        Order result = orderService.create(buildUser(), request);

        assertEquals(300.0, result.getPrice());
        assertEquals(7, product.getStock());
        assertEquals(8, product.getSellCount());
        assertEquals(1, result.getItems().size());
        assertEquals("**** **** **** 1111", result.getCardNo());
    }

    @Test
    @DisplayName("Throws InsufficientStockException and saves nothing when the stock is too low")
    void shouldFailWhenStockIsInsufficient() {
        Product product = buildProduct(10L, 100.0, 2, 0);
        when(productRepository.findById(10L)).thenReturn(Optional.of(product));

        OrderRequest request = buildRequest(500.0, List.of(new OrderRequest.OrderProductRequest(10L, 5, null)));

        assertThrows(InsufficientStockException.class, () -> orderService.create(buildUser(), request));
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    @DisplayName("Throws ResourceNotFoundException when an ordered product does not exist")
    void shouldFailWhenProductDoesNotExist() {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        OrderRequest request = buildRequest(100.0, List.of(new OrderRequest.OrderProductRequest(99L, 1, null)));

        assertThrows(ResourceNotFoundException.class, () -> orderService.create(buildUser(), request));
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    @DisplayName("Throws BadRequestException when the order contains no products")
    void shouldFailWhenOrderHasNoProducts() {
        OrderRequest request = buildRequest(0.0, List.of());

        assertThrows(BadRequestException.class, () -> orderService.create(buildUser(), request));
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    @DisplayName("Throws BadRequestException when a product count is zero or negative")
    void shouldFailWhenCountIsNotPositive() {
        OrderRequest request = buildRequest(0.0, List.of(new OrderRequest.OrderProductRequest(10L, 0, null)));

        assertThrows(BadRequestException.class, () -> orderService.create(buildUser(), request));
        verify(orderRepository, never()).save(any(Order.class));
    }
}