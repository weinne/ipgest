package br.com.ipgest.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
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

    @Override
    protected void validateEntity(Long id, Igreja entity, @ModelAttribute("selectedIgreja") Long selectedIgrejaId) {
        User user = userService.getLoggedInUser();

        if (user == null) {
            throw new AccessDeniedException("Usuário não está autenticado.");
        }

        user = userService.getUserById(user.getId());

        if (id != null) {
            Igreja existingIgreja = igrejaService.findById(id);
            if (existingIgreja == null) {
                throw new IllegalArgumentException("Igreja não encontrada para o ID: " + id);
            }

            // Verifica se o usuário tem permissão para editar esta igreja
            if (!user.getIgrejas().contains(existingIgreja)) {
                throw new AccessDeniedException("Você não tem permissão para editar esta igreja.");
            }

            if (entity.getUsers() == null) {
                entity.setUsers(new ArrayList<>());
            }
            entity.getUsers().add(user);
        } else {
            if (entity.getUsers() == null) {
                entity.setUsers(new ArrayList<>());
            }
            entity.getUsers().add(user);
        }
    }

    @Override
    protected String getEntityName() {
        return "igreja";
    }

}