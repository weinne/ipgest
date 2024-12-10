package br.com.ipgest.controller;

import br.com.ipgest.model.Igreja;
import br.com.ipgest.service.IgrejaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/igrejas")
public class IgrejaController {

    @Autowired
    private IgrejaService igrejaService;

    @GetMapping
    public List<Igreja> getAll() {
        return igrejaService.findAll();
    }

    @GetMapping("/{id}")
    public Igreja getById(@PathVariable Long id) {
        return igrejaService.findById(id);
    }

    @PostMapping
    public Igreja create(@RequestBody Igreja igreja) {
        return igrejaService.save(igreja);
    }

    @PutMapping("/{id}")
    public Igreja update(@PathVariable Long id, @RequestBody Igreja igreja) {
        igreja.setId(id);
        return igrejaService.save(igreja);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        igrejaService.deleteById(id);
    }
}
