package paulodev.orderflowapi.application.dto.response;

import paulodev.orderflowapi.domain.enums.UserStatus;

import java.util.UUID;

public record UserResponse(UUID uuid, String username, String email, UserStatus userStatus) {
}
