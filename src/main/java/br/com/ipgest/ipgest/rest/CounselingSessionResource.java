package br.com.ipgest.ipgest.rest;

import br.com.ipgest.ipgest.model.CounselingSessionDTO;
import br.com.ipgest.ipgest.service.CounselingSessionService;
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
@RequestMapping(value = "/api/counselingSessions", produces = MediaType.APPLICATION_JSON_VALUE)
public class CounselingSessionResource {

    private final CounselingSessionService counselingSessionService;

    public CounselingSessionResource(final CounselingSessionService counselingSessionService) {
        this.counselingSessionService = counselingSessionService;
    }

    @GetMapping
    public ResponseEntity<List<CounselingSessionDTO>> getAllCounselingSessions() {
        return ResponseEntity.ok(counselingSessionService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CounselingSessionDTO> getCounselingSession(
            @PathVariable(name = "id") final UUID id) {
        return ResponseEntity.ok(counselingSessionService.get(id));
    }

    @PostMapping
    public ResponseEntity<UUID> createCounselingSession(
            @RequestBody @Valid final CounselingSessionDTO counselingSessionDTO) {
        final UUID createdId = counselingSessionService.create(counselingSessionDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UUID> updateCounselingSession(@PathVariable(name = "id") final UUID id,
            @RequestBody @Valid final CounselingSessionDTO counselingSessionDTO) {
        counselingSessionService.update(id, counselingSessionDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCounselingSession(@PathVariable(name = "id") final UUID id) {
        counselingSessionService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
