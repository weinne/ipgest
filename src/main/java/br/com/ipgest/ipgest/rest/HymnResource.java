package br.com.ipgest.ipgest.rest;

import br.com.ipgest.ipgest.model.HymnDTO;
import br.com.ipgest.ipgest.service.HymnService;
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
@RequestMapping(value = "/api/hymns", produces = MediaType.APPLICATION_JSON_VALUE)
public class HymnResource {

    private final HymnService hymnService;

    public HymnResource(final HymnService hymnService) {
        this.hymnService = hymnService;
    }

    @GetMapping
    public ResponseEntity<List<HymnDTO>> getAllHymns() {
        return ResponseEntity.ok(hymnService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<HymnDTO> getHymn(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(hymnService.get(id));
    }

    @PostMapping
    public ResponseEntity<Long> createHymn(@RequestBody @Valid final HymnDTO hymnDTO) {
        final Long createdId = hymnService.create(hymnDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateHymn(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final HymnDTO hymnDTO) {
        hymnService.update(id, hymnDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHymn(@PathVariable(name = "id") final Long id) {
        hymnService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
