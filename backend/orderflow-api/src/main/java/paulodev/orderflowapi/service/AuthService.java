package paulodev.orderflowapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import paulodev.orderflowapi.dto.response.UserResponse;
import paulodev.orderflowapi.dto.request.UserRequest;
import paulodev.orderflowapi.dto.request.RegisterRequest;
import paulodev.orderflowapi.dto.response.UserTokenResponse;
import paulodev.orderflowapi.entity.User;
import paulodev.orderflowapi.repository.UserRepository;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authManager; // validate the password
    @Autowired
    private PasswordEncoder passwordEncoder; // encrypt the password
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TokenService tokenService;


    public UserTokenResponse authLogin(UserRequest userRequest) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(userRequest.username(), userRequest.password());
        var auth = authManager.authenticate(usernamePassword);
        var token = tokenService.tokenGenerate(auth.getName());
        UserTokenResponse response = new UserTokenResponse(
                token,
                auth.getName());
        return response;
    }

    public User authRegister(RegisterRequest registerRequest) {
        if (userRepository.findByUsername(registerRequest.username()) != null) {
            throw new RuntimeException("Usuário já foi criado");
        }
        String encryptedPassword = passwordEncoder.encode(registerRequest.password());
        User newUser = User.builder()
                .username(registerRequest.username())
                .password(encryptedPassword)
                .email(registerRequest.email())
                .build();
        return userRepository.save(newUser);
    }
}
