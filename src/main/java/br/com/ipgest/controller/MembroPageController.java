package br.com.ipgest.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import br.com.ipgest.model.Membro;
import br.com.ipgest.service.MembroService;

@Controller
@RequestMapping("/membros")
public class MembroPageController {

    @Autowired
    private MembroService membroService;

    @GetMapping
    public String showMembrosPage(Model model) {
        List<Membro> membros = membroService.findAll();
        model.addAttribute("membros", membros);
        model.addAttribute("isEdit", false); // Adiciona a variável isEdit ao modelo
        return "membros";
    }

    @PostMapping("/save")
    public String saveMembro(@RequestParam String nome, @RequestParam String email, @RequestParam(required = false) Long id) {
        Membro membro = new Membro();
        membro.setNome(nome);
        membro.setEmail(email);
        if (id != null) {
            membro.setId(id);
        }
        membroService.save(membro);
        return "redirect:/membros";
    }

    @PostMapping("/delete/{id}")
    public String deleteMembro(@PathVariable Long id) {
        membroService.deleteById(id);
        return "redirect:/membros";
    }

    @GetMapping("/edit/{id}")
    public String editMembro(@PathVariable Long id, Model model) {
        Membro membro = membroService.findById(id);
        List<Membro> membros = membroService.findAll();
        model.addAttribute("membros", membros);
        model.addAttribute("membro", membro);
        model.addAttribute("isEdit", true); // Adiciona a variável isEdit ao modelo
        return "membros";
    }
}
