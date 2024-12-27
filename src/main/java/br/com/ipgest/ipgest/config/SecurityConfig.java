package br.com.ipgest.ipgest.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/**", "/logout", "/auth/**", "/assets/**", "/js/**").permitAll() // Permitir acesso a login, logout e recursos estáticos
                .requestMatchers("/admin/**").hasRole("ADMIN") // Apenas usuários com papel de ADMIN podem acessar /admin
                .requestMatchers("/h2-console/**").permitAll() // Permitir acesso ao console H2
                .anyRequest().authenticated() // Qualquer outra requisição precisa estar autenticada
            )
            .formLogin(form -> form
                .loginPage("/auth/login")
                .defaultSuccessUrl("/dashboard", true) // Redirecionar para /dashboard após login bem-sucedido
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/auth/logout")
                .logoutSuccessUrl("/auth/login?logout")
                .invalidateHttpSession(true) // Invalida a sessão HTTP
                .deleteCookies("JSESSIONID") // Deleta o cookie JSESSIONID
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
