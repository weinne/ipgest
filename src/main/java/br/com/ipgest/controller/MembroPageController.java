package br.com.ipgest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import br.com.ipgest.model.Membro;
import br.com.ipgest.model.User;
import br.com.ipgest.service.IgrejaService;
import br.com.ipgest.service.MembroService;
import br.com.ipgest.service.UserService;

@Controller
@RequestMapping("/membro")
public class MembroPageController extends BasePageController<Membro, Long> {

    @Autowired
    private MembroService membroService;

    @Autowired
    protected UserService userService;

    @Autowired
    protected IgrejaService igrejaService;

    @Override
    protected Membro createNewEntity() {
        return new Membro();
    }

    @Override
    protected String getEntityFormView() {
        return "membroForm";
    }

    @Override
    protected String getEntityListView() {
        return "membroList";
    }

    @Override
    protected String getEntityName() {
        return "membro";
    }

    @Override
    protected void validateEntity(Long id, Membro membro, @ModelAttribute("selectedIgreja") Long selectedIgrejaId) {
        User user = userService.getLoggedInUser();

        if (user == null) {
            throw new AccessDeniedException("Usuário não está autenticado.");
        }

        if (id != null) {
            Membro existingMembro = membroService.findById(id);
            if (existingMembro == null) {
                throw new IllegalArgumentException("Membro não encontrado para o ID: " + id);
            }

            // Verifica se o usuário tem permissão para editar este membro
            if (existingMembro.getIgreja() == null) {
                throw new AccessDeniedException("O membro não está associado a nenhuma igreja.");
            }
            
            membro.setId(id);
        } else {
            if (selectedIgrejaId == null) {
                throw new IllegalArgumentException("O ID da igreja selecionada não pode ser nulo.");
            }
            membro.setIgreja(igrejaService.findById(selectedIgrejaId));
        }
    }
}
