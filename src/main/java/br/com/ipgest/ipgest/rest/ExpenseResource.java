package br.com.ipgest.ipgest.rest;

import br.com.ipgest.ipgest.model.ExpenseDTO;
import br.com.ipgest.ipgest.service.ExpenseService;
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
@RequestMapping(value = "/api/expenses", produces = MediaType.APPLICATION_JSON_VALUE)
public class ExpenseResource {

    private final ExpenseService expenseService;

    public ExpenseResource(final ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @GetMapping
    public ResponseEntity<List<ExpenseDTO>> getAllExpenses() {
        return ResponseEntity.ok(expenseService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExpenseDTO> getExpense(@PathVariable(name = "id") final UUID id) {
        return ResponseEntity.ok(expenseService.get(id));
    }

    @PostMapping
    public ResponseEntity<UUID> createExpense(@RequestBody @Valid final ExpenseDTO expenseDTO) {
        final UUID createdId = expenseService.create(expenseDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UUID> updateExpense(@PathVariable(name = "id") final UUID id,
            @RequestBody @Valid final ExpenseDTO expenseDTO) {
        expenseService.update(id, expenseDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpense(@PathVariable(name = "id") final UUID id) {
        expenseService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
