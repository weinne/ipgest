package br.com.ipgest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import br.com.ipgest.model.User;
import br.com.ipgest.service.UserService;

@Controller
@RequestMapping("/home")
public class HomePageController {

    @Autowired
    private UserService userService;

    @GetMapping
    public String home(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !auth.getName().equals("anonymousUser")) {
            User user = userService.getLoggedInUser();
            if (user.getIgrejas().isEmpty()) {
                return "redirect:/igreja";
            }
            return "home";
        }

        return "login"; // Renderiza a página de login
    }
}