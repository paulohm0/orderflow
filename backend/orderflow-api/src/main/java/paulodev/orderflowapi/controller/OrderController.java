package paulodev.orderflowapi.controller;

import jakarta.validation.Valid;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import paulodev.orderflowapi.dto.request.OrderRequest;
import paulodev.orderflowapi.dto.response.OrderResponse;
import paulodev.orderflowapi.entity.Order;
import paulodev.orderflowapi.entity.User;
import paulodev.orderflowapi.service.OrderService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@Valid @RequestBody OrderRequest orderRequest) {
        var newOrder = orderService.createOrder(orderRequest);
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

    @GetMapping("/list/{userId}")
    public ResponseEntity<List<OrderResponse>> getOrdersListByUserId(@PathVariable("userId") UUID userId) {
        List<OrderResponse> orderResponseList = orderService.getOrdersListByUserId(userId)
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
    public ResponseEntity<Object> cancelOrder(
            @PathVariable("orderId") UUID orderId,
            @AuthenticationPrincipal User authenticatedUser)
    {
        orderService.cancelOrder(orderId, authenticatedUser);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Pedido cancelado com sucesso");
        return ResponseEntity.ok(response);
    }
}
