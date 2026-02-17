package paulodev.orderflowapi.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import paulodev.orderflowapi.messaging.RabbitMQConfig;
import paulodev.orderflowapi.dto.request.OrderRequest;
import paulodev.orderflowapi.entity.Order;
import paulodev.orderflowapi.entity.OrderStatus;
import paulodev.orderflowapi.repository.OrderRepository;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public Order createOrder(OrderRequest orderRequest) {
        Order newOrder = Order.builder()
                .name(orderRequest.name())
                .amount(orderRequest.amount())
                .status(OrderStatus.PENDING)
                .build();
    var savedOrder = orderRepository.save(newOrder);
    rabbitTemplate.convertAndSend(RabbitMQConfig.ORDER_QUEUE,savedOrder.getId().toString());
    return savedOrder;
    }

}
