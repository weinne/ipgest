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

/**
 * BasePageController is an abstract controller class that provides common functionality 
 * for handling CRUD operations on entities. It is annotated with @Controller and 
 * @RequestMapping to map requests to the appropriate handler methods.
 *
 * @param <T>  the type of the entity that extends Identifiable
 * @param <ID> the type of the entity's identifier
 */
@Controller
@RequestMapping("/{entities}")
public abstract class BasePageController<T extends Identifiable<ID>, ID> {

    @Autowired
    protected BaseService<T, ID> service;

    /**
     * Handles GET requests to display a page with a list of entities.
     *
     * @param model the model to which attributes are added
     * @return the name of the view to be rendered
     */
    @GetMapping
    public String showPage(Model model) {
        List<T> entities = service.findAll();
        model.addAttribute("entities", entities);
        model.addAttribute("isEdit", false);
        return getEntityListView();
    }

    /**
     * Handles the GET request for creating a new entity.
     * 
     * This method is mapped to the "/new" URL and is responsible for initializing
     * a new entity, adding it to the model, and returning the view name for the
     * entity form.
     * 
     * @param model the model to which attributes are added
     * @return the name of the view for the entity form
     */
    @GetMapping("/new")
    public String newEntity(Model model) {
        T entity = createNewEntity();
        model.addAttribute("isEdit", false);
        model.addAttribute("entity", entity);
        return getEntityFormView();
    }

    /**
     * Handles the GET request for editing an entity.
     *
     * @param id    the ID of the entity to be edited
     * @param model the model to which attributes are added
     * @return the view name of the entity form
     */
    @GetMapping("/edit/{id}")
    public String editEntity(@PathVariable ID id, Model model) {
        T entity = service.findById(id);
        model.addAttribute("entity", entity);
        model.addAttribute("isEdit", true);
        return getEntityFormView();
    }

    /**
     * Handles the HTTP POST request to save an entity.
     *
     * @param id the ID of the entity to be saved, can be null for new entities
     * @param entity the entity object to be saved
     * @param selectedIgrejaId the ID of the selected "Igreja" (church)
     * @return the view name of the entity list
     */
    @PostMapping("/save")
    public String saveEntity(@RequestParam(required = false) ID id, @RequestBody T entity, @ModelAttribute("selectedIgreja") Long selectedIgrejaId) {
        validateEntity(id, entity, selectedIgrejaId);

        if (id != null) {
            entity.setId(id);
        }

        service.save(entity);
        return getEntityListView();
    }

    /**
     * Deletes an entity by its ID.
     *
     * @param id the ID of the entity to be deleted
     * @return the view name of the entity list after deletion
     */
    @PostMapping("/delete/{id}")
    public String deleteEntity(@PathVariable ID id) {
        service.deleteById(id);
        return getEntityListView();
    }

    /**
     * Creates a new instance of the entity.
     * This method should be implemented by subclasses to provide the specific entity creation logic.
     *
     * @return a new instance of the entity
     */
    protected abstract T createNewEntity();

    /**
     * Validates the given entity based on its ID and the selected church ID.
     *
     * @param id the ID of the entity to be validated
     * @param entity the entity to be validated
     * @param selectedIgrejaId the ID of the selected church
     * @throws ValidationException if the entity is not valid
     */
    protected abstract void validateEntity(ID id, T entity, Long selectedIgrejaId);

    /**
     * Returns the view name of the entity form.
     * This method should be implemented by subclasses to provide the specific view name for the entity form.
     *
     * @return the view name of the entity form
     */
    protected abstract String getEntityFormView();

    /**
     * Returns the view name for the entity list.
     * This method should be implemented by subclasses to provide the specific view name.
     *
     * @return the view name for the entity list
     */
    protected abstract String getEntityListView();
}
