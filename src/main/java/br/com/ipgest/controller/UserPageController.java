package br.com.ipgest.controller;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import br.com.ipgest.model.User;
import br.com.ipgest.repository.UserRepository;

@Controller
@RequestMapping("/usuario")
public class UserPageController {

    @Autowired
    private UserRepository userRepository;

    /**
     * Handles the registration of a new user.
     * 
     * @param user  The user object containing the registration details.
     * @param model The model object to pass attributes to the view.
     * @return The name of the view to be rendered.
     * 
     * If the username already exists, an error message is added to the model and the userForm view is returned.
     * Otherwise, the user's password is encoded, the default role is set, and the user is saved to the repository.
     * The login view is then returned.
     */
    @PostMapping("/registrar")
    public String register(@ModelAttribute User user, Model model) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            model.addAttribute("error", "Usuário já existe");
            return "userForm";
        }

        System.out.println("Usuário cadastrado: " + user);

        user.setAtivo(true);
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        user.setRoles(new HashSet<>(Set.of("ROLE_USER"))); // Define o papel padrão
        
        userRepository.save(user);
        
        // Redireciona para a página de login
        return "redirect:/login";
    }

    
    @GetMapping("/registrar")
    public String newUser(Model model) {
        return "userForm";
    }
}
