package tr.com.java.commerceplatform.order.service;

import tr.com.java.commerceplatform.order.dto.OrderRequest;
import tr.com.java.commerceplatform.order.entity.Order;
import tr.com.java.commerceplatform.user.entity.User;
import java.util.List;

public interface OrderService {
    Order create(User user, OrderRequest request);
    List<Order> getAllByUser(User user);
}