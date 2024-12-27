package br.com.ipgest.ipgest.controller;

import br.com.ipgest.ipgest.domain.Church;
import br.com.ipgest.ipgest.domain.User;
import br.com.ipgest.ipgest.model.UserChurchDTO;
import br.com.ipgest.ipgest.repos.ChurchRepository;
import br.com.ipgest.ipgest.repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collections;
import java.util.HashSet;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ChurchRepository churchRepository;

    @PostMapping("/register")
    public String register(@ModelAttribute UserChurchDTO userChurchDTO, Model model) {
        User user = userChurchDTO.getUser();
        Church church = userChurchDTO.getChurch();

        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            model.addAttribute("error", "User already exists");
            return "auth/register-form";
        }

        // Salvar a igreja
        churchRepository.save(church);

        // Associar a igreja ao usuário
        user.setChurch(church);
        user.setEnabled(true);
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        user.setRoles(new HashSet<>(Collections.singleton("ROLE_USER"))); // Define o papel padrão

        // Salvar o usuário
        userRepository.save(user);

        // Redireciona para a página de login
        return "redirect:/auth/login";
    }

    @GetMapping("/register")
    public String newUser(Model model) {
        model.addAttribute("userChurchDTO", new UserChurchDTO());
        return "auth/register-form";
    }

    @GetMapping("/login")
    public String login() {
        return "auth/login"; // Renderiza a página de login dentro da pasta auth
    }
}
