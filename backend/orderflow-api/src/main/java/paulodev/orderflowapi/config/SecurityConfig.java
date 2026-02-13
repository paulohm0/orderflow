package paulodev.orderflowapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

// desativa a proteção CSRF do Spring Security para poder usar o Postman sem muitos problemas

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // <-- Libera o POST
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().authenticated() // Exige senha para tudo
                )
                .httpBasic(Customizer.withDefaults()); // Habilita o login pelo Postman

        return http.build();
    }
}
