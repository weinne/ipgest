package br.com.ipgest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import br.com.ipgest.constants.ViewNames;
import br.com.ipgest.model.User;
import br.com.ipgest.service.UserService;

@Controller
@RequestMapping("/home")
public class HomePageController {

    @Autowired
    private UserService userService;

    @GetMapping
    public String home(Model model) {
        User user = userService.getLoggedInUser();
        String username = user.getUsername();
        model.addAttribute("username", username);
        return ViewNames.HOME;
    }
}