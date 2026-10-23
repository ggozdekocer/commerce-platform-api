package tr.com.java.commerceplatform.order.controller;

import tr.com.java.commerceplatform.order.dto.OrderRequest;
import tr.com.java.commerceplatform.order.entity.Order;
import tr.com.java.commerceplatform.user.entity.User;
import tr.com.java.commerceplatform.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public Order createOrder(@RequestBody OrderRequest request, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return orderService.create(user, request);
    }


    @GetMapping
    public List<Order> getOrders(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return orderService.getAllByUser(user);
    }
}