package paulodev.orderflowapi.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import paulodev.orderflowapi.messaging.RabbitMQConfig;
import paulodev.orderflowapi.entity.Order;
import paulodev.orderflowapi.entity.OrderStatus;
import paulodev.orderflowapi.repository.OrderRepository;
import paulodev.orderflowapi.service.SseService;

import java.util.UUID;

@Component
@Slf4j  //  to logs
public class OrderListener {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private SseService sseService;

    @RabbitListener(queues = RabbitMQConfig.ORDER_QUEUE)
    public void processOrder(String orderId) {
        log.info("Recebe o pedido: " + orderId);

        UUID id = UUID.fromString(orderId);
        Order order = orderRepository.findById(id).orElseThrow();
        log.info(order.getStatus().toString());

        // 1. Simula um processamento lento (ex: Pagamento, Estoque)
        try {
            Thread.sleep(60000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 3. Atualiza o status
        order.setStatus(OrderStatus.COMPLETED);
        sseService.notify("Pedido " + order.getId() + " atualizado para: " + order.getStatus());
        orderRepository.save(order);
        log.info("Pedido atualizado e notificação enviada !");
    }
}
