package br.com.ipgest.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import br.com.ipgest.constants.ViewNames;

@Controller
@RequestMapping("/login")
public class LoginController {
    
    /**
     * Página de login, renderiza o formulário de login
     * 
     * @return Retorna o nome da página de login
     */
    @GetMapping
    public String login() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !auth.getName().equals("anonymousUser")) {
            return ViewNames.HOME;
        }

        return ViewNames.LOGIN; // Renderiza a página de login
    }


}
