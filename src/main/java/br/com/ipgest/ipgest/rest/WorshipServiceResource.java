package br.com.ipgest.ipgest.rest;

import br.com.ipgest.ipgest.model.WorshipServiceDTO;
import br.com.ipgest.ipgest.service.WorshipServiceService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
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
@RequestMapping(value = "/api/worshipServices", produces = MediaType.APPLICATION_JSON_VALUE)
public class WorshipServiceResource {

    private final WorshipServiceService worshipServiceService;

    public WorshipServiceResource(final WorshipServiceService worshipServiceService) {
        this.worshipServiceService = worshipServiceService;
    }

    @GetMapping
    public ResponseEntity<List<WorshipServiceDTO>> getAllWorshipServices() {
        return ResponseEntity.ok(worshipServiceService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<WorshipServiceDTO> getWorshipService(
            @PathVariable(name = "id") final UUID id) {
        return ResponseEntity.ok(worshipServiceService.get(id));
    }

    @PostMapping
    public ResponseEntity<UUID> createWorshipService(
            @RequestBody @Valid final WorshipServiceDTO worshipServiceDTO) {
        final UUID createdId = worshipServiceService.create(worshipServiceDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UUID> updateWorshipService(@PathVariable(name = "id") final UUID id,
            @RequestBody @Valid final WorshipServiceDTO worshipServiceDTO) {
        worshipServiceService.update(id, worshipServiceDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWorshipService(@PathVariable(name = "id") final UUID id) {
        worshipServiceService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
