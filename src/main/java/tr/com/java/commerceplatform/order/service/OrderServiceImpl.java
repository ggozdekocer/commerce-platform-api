package tr.com.java.commerceplatform.order.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.com.java.commerceplatform.common.exception.BadRequestException;
import tr.com.java.commerceplatform.common.exception.InsufficientStockException;
import tr.com.java.commerceplatform.common.exception.ResourceNotFoundException;
import tr.com.java.commerceplatform.common.util.CardNumberMasker;
import tr.com.java.commerceplatform.order.dto.OrderRequest;
import tr.com.java.commerceplatform.order.entity.Order;
import tr.com.java.commerceplatform.order.entity.OrderItem;
import tr.com.java.commerceplatform.order.repository.OrderRepository;
import tr.com.java.commerceplatform.product.entity.Product;
import tr.com.java.commerceplatform.product.repository.ProductRepository;
import tr.com.java.commerceplatform.user.entity.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Default implementation of {@link OrderService}.
 * Placing an order is one transaction: validate, check stock, reduce stock, calculate the price
 * and save. If any step fails, every stock change made so far is rolled back.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private static final double PRICE_TOLERANCE = 0.01;

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    /**
     * {@inheritDoc}
     *
     * <p>Steps: validate the request, load each product, check and reduce its stock, increase its
     * sell count, calculate the total price on the server and save the order.
     */
    @Override
    @Transactional
    public Order create(User user, OrderRequest request) {
        validate(request);
        log.info("Creating order for user id={} with {} product lines", user.getId(), request.products().size());

        Order order = new Order();
        order.setUser(user);
        order.setAddressId(request.addressId());
        order.setOrderDate(request.orderDate());
        order.setCardNo(CardNumberMasker.mask(request.cardNo()));
        order.setCardName(request.cardName());
        order.setCardExpireMonth(request.cardExpireMonth());
        order.setCardExpireYear(request.cardExpireYear());

        List<OrderItem> items = new ArrayList<>();
        double total = 0;

        for (OrderRequest.OrderProductRequest line : request.products()) {
            Product product = productRepository.findById(line.productId())
                    .orElseThrow(() -> {
                        log.warn("Order failed: product id={} not found", line.productId());
                        return new ResourceNotFoundException("Product not found with id: " + line.productId());
                    });

            reserveStock(product, line.count());
            total += product.getPrice() * line.count();

            OrderItem item = new OrderItem();
            item.setProduct(product);
            item.setCount(line.count());
            item.setDetail(line.detail());
            item.setOrder(order);
            items.add(item);
        }

        double serverPrice = Math.round(total * 100) / 100.0;
        if (request.price() != null && Math.abs(serverPrice - request.price()) > PRICE_TOLERANCE) {
            log.warn("Price sent by the client ({}) differs from the calculated price ({}), using the calculated one",
                    request.price(), serverPrice);
        }
        order.setPrice(serverPrice);
        order.setItems(items);

        Order saved = orderRepository.save(order);
        log.info("Order id={} created for user id={} with total price {}", saved.getId(), user.getId(), serverPrice);
        return saved;
    }

    /** {@inheritDoc} */
    @Override
    public List<Order> getAllByUser(User user) {
        log.debug("Loading orders of user id={}", user.getId());
        return orderRepository.findByUserId(user.getId());
    }

    /** Rejects requests with no product lines, missing product ids or counts that are not positive. */
    private void validate(OrderRequest request) {
        if (request.products() == null || request.products().isEmpty()) {
            throw new BadRequestException("An order must contain at least one product");
        }
        for (OrderRequest.OrderProductRequest line : request.products()) {
            if (line == null || line.productId() == null) {
                throw new BadRequestException("Every order line needs a product id");
            }
            if (line.count() == null || line.count() <= 0) {
                throw new BadRequestException("Count must be greater than zero for product id=" + line.productId());
            }
        }
    }

    /** Checks that enough items are in stock, then reduces the stock and increases the sell count. */
    private void reserveStock(Product product, int count) {
        int available = product.getStock() == null ? 0 : product.getStock();
        if (available < count) {
            log.warn("Order failed: product id={} has {} in stock but {} were requested",
                    product.getId(), available, count);
            throw new InsufficientStockException("Not enough stock for product id=" + product.getId()
                    + ": requested " + count + ", available " + available);
        }
        int sold = product.getSellCount() == null ? 0 : product.getSellCount();
        product.setStock(available - count);
        product.setSellCount(sold + count);
        productRepository.save(product);
        log.debug("Stock of product id={} reduced from {} to {}", product.getId(), available, available - count);
    }
}