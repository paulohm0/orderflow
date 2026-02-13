package paulodev.orderflowapi.controller;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import paulodev.orderflowapi.dto.request.OrderRequest;
import paulodev.orderflowapi.dto.response.OrderResponse;
import paulodev.orderflowapi.service.OrderService;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@RequestBody OrderRequest orderRequest) {
        var newOrder = orderService.createOrder(orderRequest);
        OrderResponse orderResponse = new OrderResponse(
                newOrder.getName(),
                newOrder.getAmount(),
                newOrder.getStatus()
        );
        return ResponseEntity.ok(orderResponse);
    }

}
