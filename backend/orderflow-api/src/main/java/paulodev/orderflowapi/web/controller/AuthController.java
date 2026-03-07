package paulodev.orderflowapi.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import paulodev.orderflowapi.application.dto.request.UpdateUserRequest;
import paulodev.orderflowapi.application.dto.response.UserWithMessageResponse;
import paulodev.orderflowapi.application.dto.response.UserResponse;
import paulodev.orderflowapi.application.dto.request.UserRequest;
import paulodev.orderflowapi.application.dto.request.RegisterRequest;
import paulodev.orderflowapi.application.dto.response.UserTokenResponse;
import paulodev.orderflowapi.domain.entity.User;
import paulodev.orderflowapi.application.service.AuthService;

import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<UserWithMessageResponse> authRegister (@RequestBody RegisterRequest registerRequest) {
        var newUser = authService.authRegister(registerRequest);
        UserWithMessageResponse response = new UserWithMessageResponse(
                                            "User created successfully",
                                            newUser.getUsername(),
                                            newUser.getEmail());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<UserTokenResponse> authLogin (@RequestBody UserRequest userRequest) {
        var userWithAccessToken = authService.authLogin(userRequest);
        return ResponseEntity.ok(userWithAccessToken);
    }

    @GetMapping("/users-list")
    public ResponseEntity<List<UserResponse>> findAllUsersCreated(@AuthenticationPrincipal User authenticatedUser) {
        List<UserResponse> response = authService.findAllUsersCreated(authenticatedUser)
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
                                            "Updated informations",
                                            updatedUser.getUsername(),
                                            updatedUser.getEmail());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete-user")
    public ResponseEntity deleteUser(@AuthenticationPrincipal User authenticatedUser) {
        authService.deleteUser(authenticatedUser);
        return ResponseEntity.noContent().build();
    }
}
