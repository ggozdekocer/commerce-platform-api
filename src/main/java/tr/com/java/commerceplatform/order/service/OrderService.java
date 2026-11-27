package tr.com.java.commerceplatform.order.service;

import tr.com.java.commerceplatform.common.exception.BadRequestException;
import tr.com.java.commerceplatform.common.exception.InsufficientStockException;
import tr.com.java.commerceplatform.common.exception.ResourceNotFoundException;
import tr.com.java.commerceplatform.order.dto.OrderRequest;
import tr.com.java.commerceplatform.order.entity.Order;
import tr.com.java.commerceplatform.user.entity.User;

import java.util.List;

/** Business operations for creating and listing orders. */
public interface OrderService {

    /**
     * Creates an order for a user from the given request.
     *
     * @param user    the user who places the order
     * @param request the address, payment and product lines of the order
     * @return the saved order
     * @throws ResourceNotFoundException if one of the ordered products does not exist
     * @throws BadRequestException       if the order has no products or a count is not positive
     * @throws InsufficientStockException if a product does not have enough stock
     */
    Order create(User user, OrderRequest request);

    /**
     * Returns all orders of a user.
     *
     * @param user the owner of the orders
     * @return the orders of the user, empty when there are none
     */
    List<Order> getAllByUser(User user);
}