package br.com.ipgest.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import br.com.ipgest.model.Igreja;
import br.com.ipgest.model.User;
import br.com.ipgest.service.IgrejaService;
import br.com.ipgest.service.UserService;

@ControllerAdvice
public class GlobalControllerAdvice {

    @Autowired
    private IgrejaService igrejaService;

    @Autowired
    private UserService userService;

    @ModelAttribute
    public void addGlobalAttributes(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        if (username != null && !username.equals("anonymousUser")) {
            User user = (User) userService.loadUserByUsername(username);
            List<Igreja> igrejas = igrejaService.findByUser(user.getUsername());
            model.addAttribute("igrejas", igrejas);
            model.addAttribute("selectedIgreja", new Igreja()); // Adiciona o objeto selectedIgreja ao modelo
        }
    }
}