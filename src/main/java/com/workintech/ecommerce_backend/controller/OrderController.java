package com.workintech.ecommerce_backend.controller;

import com.workintech.ecommerce_backend.dto.OrderRequest;
import com.workintech.ecommerce_backend.entity.Order;
import com.workintech.ecommerce_backend.entity.User;
import com.workintech.ecommerce_backend.service.IOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {

    private final IOrderService orderService;

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