package paulodev.orderflowapi.controller;

import jakarta.validation.Valid;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import paulodev.orderflowapi.dto.request.OrderRequest;
import paulodev.orderflowapi.dto.response.OrderResponse;
import paulodev.orderflowapi.service.OrderService;

import java.util.List;
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
                newOrder.getStatus()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(orderResponse);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<OrderResponse>> getOrdersListByUserId(@PathVariable("userId") UUID userId) {
        orderService.getOrdersListByUserId(userId);

    }
}
