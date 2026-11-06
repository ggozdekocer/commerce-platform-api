package tr.com.java.commerceplatform.order.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Orders", description = "Create and list the orders of the logged-in user")
public class OrderController {

    private final OrderService orderService;

    /**
     * Creates a new order for the logged-in user.
     *
     * @param request        the address, payment and product lines of the order
     * @param authentication the authentication of the current user, filled in by Spring Security
     * @return HTTP 201 with the created order wrapped in a CommonResponse
     */
    @Operation(
            summary = "Create an order",
            description = "Creates an order for the logged-in user from the given address, payment details "
                    + "and product lines. Requires a valid token.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Order created"),
            @ApiResponse(responseCode = "400", description = "The request body is missing or malformed"),
            @ApiResponse(responseCode = "403", description = "The token is missing, invalid or expired"),
            @ApiResponse(responseCode = "404", description = "One of the ordered products does not exist")
    })
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
    @Operation(
            summary = "List my orders",
            description = "Returns all orders placed by the logged-in user. Requires a valid token.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Orders returned"),
            @ApiResponse(responseCode = "403", description = "The token is missing, invalid or expired")
    })
    @GetMapping
    public ResponseEntity<CommonResponse<List<Order>>> getOrders(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        List<Order> orders = orderService.getAllByUser(user);
        return ResponseEntity.ok(CommonResponse.success(orders, "Orders retrieved successfully"));
    }
}