package paulodev.orderflowapi.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    @Value("${api.security.token.secret}")
    private String secret;

   // GERAR TOKEN
    public String tokenGenerate(String username) {
        try {
            Algorithm algorithm  = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withIssuer("orderflow-api")            // Quem emitiu
                    .withSubject(username)                  // Dono do token
                    .withExpiresAt(genExpirationDate())     // Validade
                    .sign(algorithm);
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Erro ao gerar token", exception);
        }

    }

    // VALIDAR TOKEN
    public String tokenValidate(String token){
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer("orderflow-api")
                    .build()
                    .verify(token)
                    .getSubject();    // Devolve o username se estiver tudo ok
        } catch (JWTVerificationException exception){
            throw new RuntimeException("Token inválido ou expirado");
        }
    }

    // DEFINE EXPIRAÇÃO DO TOKEN EM 2 HRS
    private Instant genExpirationDate(){
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}
