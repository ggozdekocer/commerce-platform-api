package tr.com.java.commerceplatform.order.service;

import tr.com.java.commerceplatform.common.exception.ResourceNotFoundException;
import tr.com.java.commerceplatform.order.dto.OrderRequest;
import tr.com.java.commerceplatform.order.entity.Order;
import tr.com.java.commerceplatform.order.entity.OrderItem;
import tr.com.java.commerceplatform.product.entity.Product;
import tr.com.java.commerceplatform.user.entity.User;
import tr.com.java.commerceplatform.order.repository.OrderRepository;
import tr.com.java.commerceplatform.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    @Override
    public Order create(User user, OrderRequest request) {
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
            Product product = productRepository.findById(p.productId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + p.productId()));
            OrderItem item = new OrderItem();
            item.setProduct(product);
            item.setCount(p.count());
            item.setDetail(p.detail());
            item.setOrder(order);
            items.add(item);
        }

        order.setItems(items);
        return orderRepository.save(order);
    }

    @Override
    public List<Order> getAllByUser(User user) {
        return orderRepository.findByUserId(user.getId());
    }
}