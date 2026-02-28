package paulodev.orderflowapi.dto.response;

import paulodev.orderflowapi.entity.UserStatus;

import java.util.UUID;

public record UserResponse(UUID uuid, String username, String email, UserStatus userStatus) {
}
