package br.com.ipgest.ipgest.rest;

import br.com.ipgest.ipgest.model.ChurchDTO;
import br.com.ipgest.ipgest.service.ChurchService;
import br.com.ipgest.ipgest.util.ReferencedException;
import br.com.ipgest.ipgest.util.ReferencedWarning;
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
@RequestMapping(value = "/api/churches", produces = MediaType.APPLICATION_JSON_VALUE)
public class ChurchResource {

    private final ChurchService churchService;

    public ChurchResource(final ChurchService churchService) {
        this.churchService = churchService;
    }

    @GetMapping
    public ResponseEntity<List<ChurchDTO>> getAllChurches() {
        return ResponseEntity.ok(churchService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ChurchDTO> getChurch(@PathVariable(name = "id") final UUID id) {
        return ResponseEntity.ok(churchService.get(id));
    }

    @PostMapping
    public ResponseEntity<UUID> createChurch(@RequestBody @Valid final ChurchDTO churchDTO) {
        final UUID createdId = churchService.create(churchDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UUID> updateChurch(@PathVariable(name = "id") final UUID id,
            @RequestBody @Valid final ChurchDTO churchDTO) {
        churchService.update(id, churchDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteChurch(@PathVariable(name = "id") final UUID id) {
        final ReferencedWarning referencedWarning = churchService.getReferencedWarning(id);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        churchService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
