package paulodev.orderflowapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import paulodev.orderflowapi.dto.request.LoginRequest;
import paulodev.orderflowapi.dto.request.RegisterRequest;
import paulodev.orderflowapi.dto.response.LoginResponse;
import paulodev.orderflowapi.entity.User;
import paulodev.orderflowapi.repository.UserRepository;
import paulodev.orderflowapi.service.AuthService;
import paulodev.orderflowapi.service.TokenService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

//    @PostMapping("/login")
//    public ResponseEntity<LoginResponse> authLogin(@RequestBody LoginRequest data) {
//        var usernamePassword = new UsernamePasswordAuthenticationToken(data.username(), data.password());
//        var auth = authManager.authenticate(usernamePassword);
//        var token = tokenService.generateToken(auth.getName());
//        return ResponseEntity.ok(new LoginResponse(token, data.username(), data.email()));
//    }
//
//    @PostMapping("/register")
//    public ResponseEntity<Object> authRegister(@RequestBody RegisterRequest data) {
//
//        if(userRepository.findByUsername(data.username()) != null) return ResponseEntity.badRequest().build();
//        String encryptedPassword = passwordEncoder.encode(data.password());
//        User newUser = User.builder().username(data.username()).password(data.password()).email(data.email()).build();
//        userRepository.save(newUser);
//
//        Map<String, String> response = new HashMap<>();
//        response.put("", "Usuário criado com sucesso");
//        response.put("username", newUser.getUsername());
//        response.put("email", newUser.getEmail());
//        userRepository.save(newUser);
//
//        return ResponseEntity.status(HttpStatus.CREATED).body(response);
//    }
}
