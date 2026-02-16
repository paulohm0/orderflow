package paulodev.orderflowapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import paulodev.orderflowapi.dto.response.UserResponse;
import paulodev.orderflowapi.dto.request.UserRequest;
import paulodev.orderflowapi.dto.request.RegisterRequest;
import paulodev.orderflowapi.dto.response.UserTokenResponse;
import paulodev.orderflowapi.service.AuthService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<UserTokenResponse> authLogin(@RequestBody UserRequest userRequest) {
        var userWithAccessToken = authService.authLogin(userRequest);
        return ResponseEntity.ok(userWithAccessToken);
    }

    @PostMapping("/register")
    public ResponseEntity<Object> authRegister(@RequestBody RegisterRequest registerRequest) {
        var newUser = authService.authRegister(registerRequest);
        Map<String, String> response = new HashMap<>();
        response.put("mensage", "Usuário criado com sucesso");
        response.put("username", newUser.getUsername());
        response.put("email", newUser.getEmail());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
