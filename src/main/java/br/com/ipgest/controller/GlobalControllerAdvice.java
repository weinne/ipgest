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

/**
 * GlobalControllerAdvice is a class annotated with @ControllerAdvice to handle global 
 * configurations for controllers in the application. It provides a method to add global 
 * attributes to the model, which can be accessed by all controllers.
 * 
 * This class uses two services:
 * - IgrejaService: To fetch the list of Igreja entities associated with the authenticated user.
 * - UserService: To load user details based on the username.
 * 
 * Methods:
 * - addGlobalAttributes(Model model): This method is annotated with @ModelAttribute and is 
 *   used to add global attributes to the model. It retrieves the authenticated user's 
 *   username, fetches the corresponding User entity, and then retrieves the list of Igreja 
 *   entities associated with that user. These attributes are then added to the model.
 * 
 * Attributes added to the model:
 * - "igrejas": List of Igreja entities associated with the authenticated user.
 * - "selectedIgreja": A new Igreja object to be used as a placeholder in the model.
 * 
 * Dependencies:
 * - IgrejaService: Autowired to fetch Igreja entities.
 * - UserService: Autowired to load user details.
 */
@ControllerAdvice
public class GlobalControllerAdvice {

    @Autowired
    private IgrejaService igrejaService;

    @Autowired
    private UserService userService;

    /**
     * Adds global attributes to the model for use in views.
     * 
     * This method retrieves the authenticated user's details and adds a list of 
     * churches (igrejas) associated with the user to the model. It also adds a 
     * new Igreja object to the model as "selectedIgreja".
     * 
     * @param model the model to which attributes are added
     */
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