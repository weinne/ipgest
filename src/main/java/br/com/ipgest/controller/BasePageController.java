package br.com.ipgest.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import br.com.ipgest.model.Identifiable;
import br.com.ipgest.service.BaseService;

@Controller
@RequestMapping("/{entities}")
public abstract class BasePageController<T extends Identifiable<ID>, ID> {

    @Autowired
    protected BaseService<T, ID> service;

    @GetMapping
    public String showPage(Model model) {
        List<T> entities = service.findAll();
        model.addAttribute("entities", entities);
        model.addAttribute("isEdit", false);
        return getEntityListView();
    }

    @GetMapping("/new")
    public String newEntity(Model model) {
        T entity = createNewEntity();
        model.addAttribute("isEdit", false);
        model.addAttribute("entity", entity);
        return getEntityFormView();
    }

    @GetMapping("/edit/{id}")
    public String editEntity(@PathVariable ID id, Model model) {
        T entity = service.findById(id);
        model.addAttribute("entity", entity);
        model.addAttribute("isEdit", true);
        return getEntityFormView();
    }

    @PostMapping("/save")
    public String saveEntity(@RequestParam(required = false) ID id, @RequestBody T entity, @ModelAttribute("selectedIgreja") Long selectedIgrejaId) {
        validateEntity(id, entity, selectedIgrejaId);

        if (id != null) {
            entity.setId(id);
        }

        service.save(entity);
        return getEntityListView();
    }

    @PostMapping("/delete/{id}")
    public String deleteEntity(@PathVariable ID id) {
        service.deleteById(id);
        return getEntityListView();
    }

    protected abstract T createNewEntity();

    protected abstract void validateEntity(ID id, T entity, Long selectedIgrejaId);

    protected abstract String getEntityFormView();

    protected abstract String getEntityListView();
}
