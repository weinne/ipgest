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

import br.com.ipgest.model.Identifiable;
import br.com.ipgest.service.BaseService;

/**
 * BaseController is an abstract class that provides basic CRUD operations for entities.
 * It uses a generic type T which extends Identifiable<ID> and a generic ID type.
 * 
 * @param <T>  the type of the entity
 * @param <ID> the type of the entity's identifier
 * 
 * @author 
 */
@RestController
@RequestMapping("/api/{entities}")
public abstract class BaseController<T extends Identifiable<ID>, ID> {

    @Autowired
    protected BaseService<T, ID> service;

    /**
     * Retrieves all entities.
     * 
     * @return a list of all entities
     */
    @GetMapping
    public List<T> getAll() {
        return service.findAll();
    }

    /**
     * Retrieves an entity by its ID.
     * 
     * @param id the ID of the entity
     * @return the entity with the specified ID
     */
    @GetMapping("/{id}")
    public T getById(@PathVariable ID id) {
        return service.findById(id);
    }

    /**
     * Creates a new entity.
     * 
     * @param entity the entity to create
     * @return the created entity
     */
    @PostMapping
    public T create(@RequestBody T entity) {
        return service.save(entity);
    }

    /**
     * Updates an existing entity.
     * 
     * @param id     the ID of the entity to update
     * @param entity the updated entity
     * @return the updated entity
     */
    @PutMapping("/{id}")
    public T update(@PathVariable ID id, @RequestBody T entity) {
        entity.setId(id);
        return service.save(entity);
    }

    /**
     * Deletes an entity by its ID.
     * 
     * @param id the ID of the entity to delete
     */
    @DeleteMapping("/{id}")
    public void delete(@PathVariable ID id) {
        service.deleteById(id);
    }
}
