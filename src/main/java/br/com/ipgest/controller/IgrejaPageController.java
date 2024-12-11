package br.com.ipgest.controller;

import java.util.ArrayList;

import br.com.ipgest.constants.ViewNames;
import br.com.ipgest.model.Igreja;
import br.com.ipgest.service.IgrejaService;
import br.com.ipgest.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import br.com.ipgest.model.User;


@Controller
@RequestMapping("/igreja")
public class IgrejaPageController {

    @Autowired
    private IgrejaService igrejaService;

    @Autowired
    private UserService userService;

    @PostMapping("/registrar")
    public String cadastrarIgreja(@ModelAttribute Igreja igreja, @RequestParam Long userId, Model model) {
        User user = userService.getLoggedInUser();

        igreja.getUsuarios().add(user);

        try {
            igrejaService.save(igreja);
            user.setIgreja(igreja);
            userService.save(user);
        } catch (Exception e) {
            System.out.println(e);
            return "redirect:/igreja/registrar?error=true";
        }
        return "redirect:/login";
    }

    @RequestMapping("/registrar")
    public String showForm(Model model) {
        return ViewNames.IGREJA_FORM;
    }
}