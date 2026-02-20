package paulodev.orderflowapi.dto.response;

import java.util.UUID;

public record UserResponse(UUID uuid, String username, String email) {
}
