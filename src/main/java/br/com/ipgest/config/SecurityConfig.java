package br.com.ipgest.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/newuser", "/register").permitAll() // Permitir acesso à criação de usuários
            .requestMatchers("/home").authenticated() // Apenas usuários autenticados podem acessar
            .requestMatchers("/admin/**").hasRole("ADMIN")  // Apenas usuários com a role 'ADMIN' podem acessar '/admin'
            .anyRequest().permitAll() // Todas as outras requisições precisam de autenticação
        )
        .formLogin(form -> form
            .loginPage("/login")
            .defaultSuccessUrl("/home", true) // Redirecionar para /home após login bem-sucedido
            .failureUrl("/login?error=true") // Redireciona para a página de login com erro
            .permitAll()
        )
        .logout(logout -> logout
            .logoutUrl("/logout") // URL para logout
            .logoutSuccessUrl("/login?logout") // Redirecionamento após o logout
            .invalidateHttpSession(true) // Invalida a sessão
            .deleteCookies("JSESSIONID") // Remove cookies relacionados
            .permitAll()
        )
        .sessionManagement(session -> session
            .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED) // Criar sessão se necessário
        );

    return http.build();
}


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
