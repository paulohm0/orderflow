package paulodev.orderflowapi.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.UUID;

public record OrderRequest(
        UUID userId,
        @NotBlank String description,
        @NotNull @Positive BigDecimal amount
) { }
