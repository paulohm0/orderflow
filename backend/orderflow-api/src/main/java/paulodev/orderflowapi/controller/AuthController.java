package paulodev.orderflowapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import paulodev.orderflowapi.dto.request.UpdateUserRequest;
import paulodev.orderflowapi.dto.response.MessageResponse;
import paulodev.orderflowapi.dto.response.UserWithMessageResponse;
import paulodev.orderflowapi.dto.response.UserResponse;
import paulodev.orderflowapi.dto.request.UserRequest;
import paulodev.orderflowapi.dto.request.RegisterRequest;
import paulodev.orderflowapi.dto.response.UserTokenResponse;
import paulodev.orderflowapi.entity.User;
import paulodev.orderflowapi.service.AuthService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<UserWithMessageResponse> authRegister (@RequestBody RegisterRequest registerRequest) {
        var newUser = authService.authRegister(registerRequest);
        UserWithMessageResponse response = new UserWithMessageResponse(
                                            newUser.getUsername(),
                                            newUser.getEmail(),
                                            "User created successfully");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<UserTokenResponse> authLogin (@RequestBody UserRequest userRequest) {
        var userWithAccessToken = authService.authLogin(userRequest);
        return ResponseEntity.ok(userWithAccessToken);
    }

    @GetMapping("/users-list")
    public ResponseEntity<List<UserResponse>> findAllUsersCreated() {
        List<UserResponse> response = authService.findAllUsersCreated()
                .stream().map(user ->
                        new UserResponse(
                         user.getId(),
                         user.getUsername(),
                         user.getEmail(),
                         user.getUserStatus())).toList();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/update-user")
    public ResponseEntity<UserWithMessageResponse> updateUser(
            @RequestBody UpdateUserRequest updateUserRequest,
            @AuthenticationPrincipal User authenticatedUser)
    {
        var updatedUser = authService.updateUser(authenticatedUser, updateUserRequest);
        UserWithMessageResponse response = new UserWithMessageResponse(
                                            updatedUser.getUsername(),
                                            updatedUser.getEmail(),
                                            "Updated informations");
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete-user")
    public ResponseEntity<MessageResponse> deleteUser(@AuthenticationPrincipal User authenticatedUser) {
        authService.deleteUser(authenticatedUser);
        MessageResponse response = new MessageResponse("User deleted successfully");
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
    }
}
