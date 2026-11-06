package tr.com.java.commerceplatform.order.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tr.com.java.commerceplatform.common.exception.ResourceNotFoundException;
import tr.com.java.commerceplatform.order.dto.OrderRequest;
import tr.com.java.commerceplatform.order.entity.Order;
import tr.com.java.commerceplatform.order.entity.OrderItem;
import tr.com.java.commerceplatform.order.repository.OrderRepository;
import tr.com.java.commerceplatform.product.entity.Product;
import tr.com.java.commerceplatform.product.repository.ProductRepository;
import tr.com.java.commerceplatform.user.entity.User;

import java.util.ArrayList;
import java.util.List;

/** Default implementation of {@link OrderService} backed by the order and product repositories. */
@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    /**
     * {@inheritDoc}
     *
     * @throws ResourceNotFoundException if one of the ordered products does not exist
     */
    @Override
    public Order create(User user, OrderRequest request) {
        log.info("Creating order for user id={} with {} product lines", user.getId(), request.products().size());

        Order order = new Order();
        order.setUser(user);
        order.setAddressId(request.addressId());
        order.setOrderDate(request.orderDate());
        order.setPrice(request.price());
        order.setCardNo(request.cardNo());
        order.setCardName(request.cardName());
        order.setCardExpireMonth(request.cardExpireMonth());
        order.setCardExpireYear(request.cardExpireYear());

        List<OrderItem> items = new ArrayList<>();
        for (OrderRequest.OrderProductRequest p : request.products()) {
            log.debug("Adding product id={} with count={} to the order", p.productId(), p.count());
            Product product = productRepository.findById(p.productId())
                    .orElseThrow(() -> {
                        log.warn("Order failed: product id={} not found", p.productId());
                        return new ResourceNotFoundException("Product not found with id: " + p.productId());
                    });
            OrderItem item = new OrderItem();
            item.setProduct(product);
            item.setCount(p.count());
            item.setDetail(p.detail());
            item.setOrder(order);
            items.add(item);
        }

        order.setItems(items);
        Order saved = orderRepository.save(order);
        log.info("Order id={} created for user id={}", saved.getId(), user.getId());
        return saved;
    }

    /** {@inheritDoc} */
    @Override
    public List<Order> getAllByUser(User user) {
        log.debug("Loading orders of user id={}", user.getId());
        return orderRepository.findByUserId(user.getId());
    }
}