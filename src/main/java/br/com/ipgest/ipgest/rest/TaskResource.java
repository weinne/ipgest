package br.com.ipgest.ipgest.rest;

import br.com.ipgest.ipgest.model.TaskDTO;
import br.com.ipgest.ipgest.service.TaskService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/api/tasks", produces = MediaType.APPLICATION_JSON_VALUE)
public class TaskResource {

    private final TaskService taskService;

    public TaskResource(final TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public ResponseEntity<List<TaskDTO>> getAllTasks() {
        return ResponseEntity.ok(taskService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskDTO> getTask(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(taskService.get(id));
    }

    @PostMapping
    public ResponseEntity<Long> createTask(@RequestBody @Valid final TaskDTO taskDTO) {
        final Long createdId = taskService.create(taskDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateTask(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final TaskDTO taskDTO) {
        taskService.update(id, taskDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable(name = "id") final Long id) {
        taskService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
