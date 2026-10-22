package tr.com.java.commerceplatform.order.service;

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
public class OrderServiceImpl implements IOrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    @Override
    public Order create(User user, OrderRequest request) {
        Order order = new Order();
        order.setUser(user);
        order.setAddressId(request.getAddressId());
        order.setOrderDate(request.getOrderDate());
        order.setPrice(request.getPrice());
        order.setCardNo(request.getCardNo());
        order.setCardName(request.getCardName());
        order.setCardExpireMonth(request.getCardExpireMonth());
        order.setCardExpireYear(request.getCardExpireYear());

        List<OrderItem> items = new ArrayList<>();
        for (OrderRequest.OrderProductRequest p : request.getProducts()) {
            Product product = productRepository.findById(p.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));
            OrderItem item = new OrderItem();
            item.setProduct(product);
            item.setCount(p.getCount());
            item.setDetail(p.getDetail());
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