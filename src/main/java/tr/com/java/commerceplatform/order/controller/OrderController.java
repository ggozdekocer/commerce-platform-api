package tr.com.java.commerceplatform.order.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tr.com.java.commerceplatform.common.response.CommonResponse;
import tr.com.java.commerceplatform.order.dto.OrderRequest;
import tr.com.java.commerceplatform.order.entity.Order;
import tr.com.java.commerceplatform.order.service.OrderService;
import tr.com.java.commerceplatform.user.entity.User;

import java.util.List;

/** REST endpoints for creating and listing the orders of the logged-in user. */
@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    /**
     * Creates a new order for the logged-in user.
     *
     * @param request        the address, payment and product lines of the order
     * @param authentication the authentication of the current user, filled in by Spring Security
     * @return HTTP 201 with the created order wrapped in a CommonResponse
     */
    @PostMapping
    public ResponseEntity<CommonResponse<Order>> createOrder(@RequestBody OrderRequest request,
                                                             Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Order order = orderService.create(user, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CommonResponse.success(order, "Order created successfully", HttpStatus.CREATED));
    }

    /**
     * Returns all orders of the logged-in user.
     *
     * @param authentication the authentication of the current user, filled in by Spring Security
     * @return HTTP 200 with the order list wrapped in a CommonResponse
     */
    @GetMapping
    public ResponseEntity<CommonResponse<List<Order>>> getOrders(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        List<Order> orders = orderService.getAllByUser(user);
        return ResponseEntity.ok(CommonResponse.success(orders, "Orders retrieved successfully"));
    }
}