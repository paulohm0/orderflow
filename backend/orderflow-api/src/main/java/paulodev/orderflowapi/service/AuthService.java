package paulodev.orderflowapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import paulodev.orderflowapi.dto.request.UpdateUserRequest;
import paulodev.orderflowapi.dto.request.UserRequest;
import paulodev.orderflowapi.dto.request.RegisterRequest;
import paulodev.orderflowapi.dto.response.UserTokenResponse;
import paulodev.orderflowapi.entity.User;
import paulodev.orderflowapi.entity.UserStatus;
import paulodev.orderflowapi.repository.UserRepository;

import java.util.List;
import java.util.Optional;

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
        UserTokenResponse response = new UserTokenResponse(token, auth.getName());
        return response;
    }

    public User authRegister(RegisterRequest registerRequest) {
        if (userRepository.findByUsername(registerRequest.username()) == null) {
            throw new RuntimeException("Usuário já foi criado");
        }
        String encryptedPassword = passwordEncoder.encode(registerRequest.password());
        User newUser = User.createUser(registerRequest);
        return userRepository.save(newUser);
    }

    public List<User> findAllUsersCreated() {
        var userList = userRepository.findAll();
        if (userList.isEmpty()) {
            throw new RuntimeException("Lista de Usuários vazia");
        }
        return userList;
    }

    public User updateUser(User authenticatedUser, UpdateUserRequest updateUserRequest) {

        var userToUpdate = userRepository.findById(authenticatedUser.getId())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        if (!authenticatedUser.getId().equals(userToUpdate.getId())) {
            throw new AccessDeniedException("Você não pode alterar outro usuário");
        }
        // verifico quais campos do updateUserRequest estão preenchidos e atualizo na entidade do db
        Optional.ofNullable(updateUserRequest.username()).ifPresent(userToUpdate::setUsername);
        Optional.ofNullable(updateUserRequest.email()).ifPresent(userToUpdate::setEmail);
        Optional.ofNullable(updateUserRequest.password()).ifPresent(
                password -> userToUpdate.setPassword(passwordEncoder.encode(password)));
        return userRepository.save(userToUpdate);
    }

    public void deleteUser(User authenticatedUser) {
        var user = userRepository.findById(authenticatedUser.getId())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        if (!authenticatedUser.getId().equals(user.getId())) {
            throw new AccessDeniedException("Você não pode deletar outro usuário");
        }
        if (user.getOrders().isEmpty()) {
            userRepository.deleteById(user.getId());
        } else {
            user.setUserStatus(UserStatus.INACTIVE);
        }
        userRepository.save(user);
    }
}
