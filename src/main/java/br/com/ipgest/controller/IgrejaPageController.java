package br.com.ipgest.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import br.com.ipgest.model.Igreja;
import br.com.ipgest.model.User;
import br.com.ipgest.service.IgrejaService;
import br.com.ipgest.service.UserService;


@Controller
@RequestMapping("/igreja")
public class IgrejaPageController extends BasePageController<Igreja, Long> {

    @Autowired
    private IgrejaService igrejaService;

    @Autowired
    private UserService userService;

    @Override
    protected Igreja createNewEntity() {
        return new Igreja();
    }

    @Override
    protected String getEntityFormView() {
        return "igrejaForm";
    }

    @Override
    protected String getEntityListView() {
        return "igrejaList";
    }

    @ModelAttribute
    public void addAttributes(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        List<Igreja> igrejas = igrejaService.findByUser(username);
        model.addAttribute("igrejas", igrejas);
        model.addAttribute("selectedIgreja", new Igreja()); // Adiciona o objeto selectedIgreja ao modelo
    }

    @Override
    protected void validateEntity(Long id, Igreja entity, @ModelAttribute("selectedIgreja") Long selectedIgrejaId) {
        User user = userService.getLoggedInUser();
        user = userService.getUserById(user.getId());

         if (user == null) {
            throw new AccessDeniedException("Usuário não está autenticado.");
        }

        if (id != null) {
            Igreja existingIgreja = igrejaService.findById(id);
            if (existingIgreja == null) {
                throw new IllegalArgumentException("Igreja não encontrada para o ID: " + id);
            }

            // Verifica se o usuário tem permissão para editar esta igreja
            if (!user.getIgrejas().contains(existingIgreja)) {
                throw new AccessDeniedException("Você não tem permissão para editar esta igreja.");
            }
        } else {
            entity.getUsers().add(user);
        }
    }

}