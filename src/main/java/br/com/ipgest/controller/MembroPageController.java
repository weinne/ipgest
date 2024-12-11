package br.com.ipgest.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import br.com.ipgest.model.Membro;
import br.com.ipgest.model.User;
import br.com.ipgest.service.MembroService;
import br.com.ipgest.service.UserService;

@Controller
@RequestMapping("/membros")
public class MembroPageController {

    @Autowired
    private MembroService membroService;

    @Autowired
    private UserService userService;

    @GetMapping
    public String showMembrosPage(Model model) {
        List<Membro> membros = membroService.findAll();
        model.addAttribute("membros", membros);
        model.addAttribute("isEdit", false); // Adiciona a variável isEdit ao modelo
        return "membros";
    }

    @GetMapping("/new")
    public String newMembro(@RequestParam(required = false) Long id, Model model) {
        Membro membro = new Membro();
        model.addAttribute("isEdit", false);
        model.addAttribute("membro", membro);
        return "membroForm";
    }

    @GetMapping("/edit/{id}")
    public String editMembro(@PathVariable Long id, Model model) {
        Membro membro = membroService.findById(id);
        model.addAttribute("membro", membro);
        model.addAttribute("isEdit", true); // Adiciona a variável isEdit ao modelo
        return "membroForm";
    }

    @PostMapping("/save")
    public String saveMembro(@RequestParam(required = false) Long id, @RequestParam String nome, @RequestParam String email) {
        Membro membro;
        User user = userService.getLoggedInUser();
        
        if (user == null) {
            throw new AccessDeniedException("Usuário não está autenticado.");
        }
        
        if (id != null) {
            membro = membroService.findById(id);
            if (membro == null) {
                throw new IllegalArgumentException("Membro não encontrado para o ID: " + id);
            }
            System.out.println("Membro encontrado: " + membro);
            System.out.println("Igreja do membro: " + membro.getIgreja());
            // Verifica se o usuário tem permissão para editar este membro
            if (membro.getIgreja() == null) {
                throw new AccessDeniedException("O membro não está associado a nenhuma igreja.");
            }
            if (!membro.getIgreja().equals(user.getIgreja())) {
                throw new AccessDeniedException("Você não tem permissão para editar este membro.");
            }
        } else {
            membro = new Membro();
            membro.setIgreja(user.getIgreja());
            System.out.println("Novo membro criado: " + membro);
            System.out.println("Igreja do novo membro: " + membro.getIgreja());
        }
        
        membro.setNome(nome);
        membro.setEmail(email);
        membroService.save(membro);
        return "redirect:/membros";
    }

    @PostMapping("/delete/{id}")
    public String deleteMembro(@PathVariable Long id) {
        membroService.deleteById(id);
        return "redirect:/membros";
    }
}
