package br.com.ipgest.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import br.com.ipgest.service.UserService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationSuccessHandler loginSuccessHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/login", "/logout", "/css/**", "/js/**").permitAll() // Permitir acesso a login, logout e recursos estáticos
                .requestMatchers("/admin/**").hasRole("ADMIN") // Apenas usuários com papel de ADMIN podem acessar /admin
                .requestMatchers("/usuario/**").permitAll() // Permitir acesso ao registro de usuário
                .requestMatchers("/h2-console/**").permitAll() // Permitir acesso ao console H2
                .anyRequest().authenticated() // Qualquer outra requisição precisa estar autenticada
            )
            .formLogin(form -> form
                .loginPage("/login")
                .successHandler(loginSuccessHandler) // Usar o handler de sucesso de login
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout")
                .permitAll()
            )
            .csrf(csrf -> csrf
                .ignoringRequestMatchers("/h2-console/**") // Ignorar CSRF para o console do H2
            )
            .headers(headers -> headers
                .frameOptions(frameOptions -> frameOptions
                    .sameOrigin() // Permitir que o console do H2 seja carregado em um iframe
                )
            );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
