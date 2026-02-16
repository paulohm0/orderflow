package paulodev.orderflowapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import paulodev.orderflowapi.dto.request.LoginRequest;
import paulodev.orderflowapi.dto.request.RegisterRequest;
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


//    public authLogin(LoginRequest loginRequest) {}
//
//    public authRegister(RegisterRequest registerRequest) {}
}
