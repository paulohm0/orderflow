package paulodev.orderflowapi.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import paulodev.orderflowapi.entity.User;
import paulodev.orderflowapi.esception.ForbiddenOperationException;
import paulodev.orderflowapi.esception.InvalidOperationException;
import paulodev.orderflowapi.esception.ResourceNotFoundException;
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

    public Order createOrder(OrderRequest orderRequest, User authenticatedUser) {
        User orderUser = userRepository.findById(authenticatedUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuário do pedido não localizado ou token expirado"));
        Order newOrder = Order.createOrder(orderRequest,orderUser);
        var savedOrder = orderRepository.save(newOrder);
        rabbitTemplate.convertAndSend(RabbitMQConfig.ORDER_QUEUE,savedOrder.getId().toString());
        return savedOrder;
    }

    public List<Order> getOrdersListByUserId(User authenticatedUser) {
        User user = userRepository.findById(authenticatedUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não localizado ou token expirado"));
        return user.getOrders();
    }

    public Order cancelOrder(UUID orderId, User authenticatedUser) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido ("+ orderId +") não localizado ou token expirado"));
        if (!authenticatedUser.getId().equals(order.getUser().getId())) {
            throw new ForbiddenOperationException("Você não pode cancelar um pedido de outro usuário");
        }
        if (order.getStatus() == OrderStatus.COMPLETED || order.getStatus() == OrderStatus.CANCELED) {
            throw new InvalidOperationException("O pedido ja consta como "+ order.getStatus() +", não pode ser cancelado");
        }
        order.setStatus(OrderStatus.CANCELED);
        return orderRepository.save(order);
    }
}
