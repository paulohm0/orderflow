package paulodev.orderflowapi.dto.response;

import paulodev.orderflowapi.entity.OrderStatus;

import java.math.BigDecimal;

public record OrderResponse(String name, BigDecimal amount, OrderStatus status) {
}
