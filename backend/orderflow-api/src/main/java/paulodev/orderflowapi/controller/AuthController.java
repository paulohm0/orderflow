package paulodev.orderflowapi.controller;

import org.aspectj.weaver.patterns.IToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import paulodev.orderflowapi.dto.request.UpdateUserRequest;
import paulodev.orderflowapi.dto.response.UserResponse;
import paulodev.orderflowapi.dto.request.UserRequest;
import paulodev.orderflowapi.dto.request.RegisterRequest;
import paulodev.orderflowapi.dto.response.UserTokenResponse;
import paulodev.orderflowapi.entity.User;
import paulodev.orderflowapi.service.AuthService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<Object> authRegister (@RequestBody RegisterRequest registerRequest) {
        var newUser = authService.authRegister(registerRequest);
        Map<String, String> response = new HashMap<>();
        response.put("mensage", "Usuário criado com sucesso");
        response.put("username", newUser.getUsername());
        response.put("email", newUser.getEmail());
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
                                user.getEmail())).toList();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/update-user")
    public ResponseEntity<Object> updateUser(
            @RequestBody UpdateUserRequest updateUserRequest,
            @AuthenticationPrincipal User authenticatedUser)
    {
        var updatedUser = authService.updateUser(authenticatedUser, updateUserRequest);
        Map<String, String> response = new HashMap<>();
        response.put("mensage", "Usuário atualizado com sucesso");
        response.put("username", updatedUser.getUsername());
        response.put("email", updatedUser.getEmail());
        response.put("password", "senha atualizada");
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete-user")
    public ResponseEntity<Object> deleteUser(
            @AuthenticationPrincipal User authenticatedUser)
    {
        authService.deleteUser(authenticatedUser);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Usuário deletado com sucesso");
        return ResponseEntity.ok(response);
    }
}
