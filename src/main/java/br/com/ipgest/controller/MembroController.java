package br.com.ipgest.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.ipgest.model.Membro;
import br.com.ipgest.service.MembroService;

@RestController
@RequestMapping("/api/membros")
public class MembroController {

    @Autowired
    private MembroService membroService;

    @GetMapping
    public List<Membro> getAll() {
        return membroService.findAll();
    }

    @GetMapping("/{id}")
    public Membro getById(@PathVariable Long id) {
        return membroService.findById(id);
    }

    @PostMapping
    public Membro create(@RequestBody Membro membro) {
        return membroService.save(membro);
    }

    @PutMapping("/{id}")
    public Membro update(@PathVariable Long id, @RequestBody Membro membro) {
        membro.setId(id);
        return membroService.save(membro);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        membroService.deleteById(id);
    }
}
