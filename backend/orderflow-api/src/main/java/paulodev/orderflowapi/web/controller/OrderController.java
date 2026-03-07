package paulodev.orderflowapi.web.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import paulodev.orderflowapi.application.dto.request.OrderRequest;
import paulodev.orderflowapi.application.dto.response.OrderResponse;
import paulodev.orderflowapi.domain.enums.OrderStatus;
import paulodev.orderflowapi.domain.entity.User;
import paulodev.orderflowapi.application.service.OrderService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(
            @Valid @RequestBody OrderRequest orderRequest,
            @AuthenticationPrincipal User authenticatedUser)
    {
        var newOrder = orderService.createOrder(orderRequest, authenticatedUser);
        OrderResponse orderResponse = new OrderResponse(
                                        newOrder.getId(),
                                        newOrder.getUser().getId(),
                                        newOrder.getUser().getUsername(),
                                        newOrder.getDescription(),
                                        newOrder.getAmount(),
                                        newOrder.getCreatedAt(),
                                        newOrder.getStatus());
        return ResponseEntity.status(HttpStatus.CREATED).body(orderResponse);
    }

    @GetMapping("/list")
    public ResponseEntity<List<OrderResponse>> getOrdersListByUserId(
            @AuthenticationPrincipal User authenticatedUser,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) OrderStatus status)
    {
        List<OrderResponse> orderResponseList = orderService.getOrdersListByUserId(authenticatedUser, description, status)
                .stream().map(order ->
                    new OrderResponse(
                      order.getId(),
                      order.getUser().getId(),
                      order.getUser().getUsername(),
                      order.getDescription(),
                      order.getAmount(),
                      order.getCreatedAt(),
                      order.getStatus())).toList();
        return ResponseEntity.ok(orderResponseList);
    }

    @PatchMapping("/cancel/{orderId}")
    public ResponseEntity cancelOrder(
            @PathVariable("orderId") UUID orderId,
            @AuthenticationPrincipal User authenticatedUser)
    {
        orderService.cancelOrder(orderId, authenticatedUser);
        return ResponseEntity.noContent().build();
    }
}
