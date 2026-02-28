package paulodev.orderflowapi.entity;

import jakarta.persistence.*;
import lombok.*;
import paulodev.orderflowapi.dto.request.OrderRequest;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tb_orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private OrderStatus status;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public static Order createOrder(OrderRequest orderRequest, User user) {
        Order order = new Order();
        order.description = orderRequest.description();
        order.amount = orderRequest.amount();
        order.createdAt = Instant.now();
        order.status = OrderStatus.PENDING;
        order.user = user;
        return order;
    }

}
