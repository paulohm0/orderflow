package paulodev.orderflowapi.dto.response;

import paulodev.orderflowapi.entity.OrderStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

/*
    Expondo para a tela apenas informações relevantes para o pedido
    sem expor  dados sensiveis do usuário como email, senha, etc.
*/

public record OrderResponse(
        UUID id,
        UUID userId,
        String username,
        String description,
        BigDecimal amount,
        Instant createdAt,
        OrderStatus status
) { }
