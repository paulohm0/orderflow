package paulodev.orderflowapi.dto.request;

import paulodev.orderflowapi.entity.Order;

import java.math.BigDecimal;

public record OrderRequest(String name, BigDecimal amount) {
}
