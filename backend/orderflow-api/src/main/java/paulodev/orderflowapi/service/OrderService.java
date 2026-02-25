package paulodev.orderflowapi.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import paulodev.orderflowapi.entity.User;
import paulodev.orderflowapi.messaging.RabbitMQConfig;
import paulodev.orderflowapi.dto.request.OrderRequest;
import paulodev.orderflowapi.entity.Order;
import paulodev.orderflowapi.entity.OrderStatus;
import paulodev.orderflowapi.repository.OrderRepository;
import paulodev.orderflowapi.repository.UserRepository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class OrderService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public Order createOrder(OrderRequest orderRequest) {
        User orderUser = userRepository.findById(orderRequest.userId())
                .orElseThrow(() -> new RuntimeException("Usuário não achado com id: " + orderRequest.userId()));
        Order newOrder = Order.builder()
                .description(orderRequest.description())
                .amount(orderRequest.amount())
                .createdAt(Instant.now())
                .status(OrderStatus.PENDING)
                .user(orderUser)
                .build();
    var savedOrder = orderRepository.save(newOrder);
    rabbitTemplate.convertAndSend(RabbitMQConfig.ORDER_QUEUE,savedOrder.getId().toString());
    return savedOrder;
    }

    public List<Order> getOrdersListByUserId(UUID userId) {
        User ordersUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuário não achado com id: " + userId));
        return ordersUser.getOrders();
    }
}
