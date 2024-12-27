package br.com.ipgest.ipgest.rest;

import br.com.ipgest.ipgest.model.IncomeDTO;
import br.com.ipgest.ipgest.service.IncomeService;
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
@RequestMapping(value = "/api/incomes", produces = MediaType.APPLICATION_JSON_VALUE)
public class IncomeResource {

    private final IncomeService incomeService;

    public IncomeResource(final IncomeService incomeService) {
        this.incomeService = incomeService;
    }

    @GetMapping
    public ResponseEntity<List<IncomeDTO>> getAllIncomes() {
        return ResponseEntity.ok(incomeService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<IncomeDTO> getIncome(@PathVariable(name = "id") final UUID id) {
        return ResponseEntity.ok(incomeService.get(id));
    }

    @PostMapping
    public ResponseEntity<UUID> createIncome(@RequestBody @Valid final IncomeDTO incomeDTO) {
        final UUID createdId = incomeService.create(incomeDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UUID> updateIncome(@PathVariable(name = "id") final UUID id,
            @RequestBody @Valid final IncomeDTO incomeDTO) {
        incomeService.update(id, incomeDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIncome(@PathVariable(name = "id") final UUID id) {
        incomeService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
