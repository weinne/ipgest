package br.com.ipgest.ipgest.service;

import br.com.ipgest.ipgest.domain.Church;
import br.com.ipgest.ipgest.domain.Expense;
import br.com.ipgest.ipgest.model.ExpenseDTO;
import br.com.ipgest.ipgest.repos.ChurchRepository;
import br.com.ipgest.ipgest.repos.ExpenseRepository;
import br.com.ipgest.ipgest.util.NotFoundException;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final ChurchRepository churchRepository;

    public ExpenseService(final ExpenseRepository expenseRepository,
            final ChurchRepository churchRepository) {
        this.expenseRepository = expenseRepository;
        this.churchRepository = churchRepository;
    }

    public List<ExpenseDTO> findAll() {
        final List<Expense> expenses = expenseRepository.findAll(Sort.by("id"));
        return expenses.stream()
                .map(expense -> mapToDTO(expense, new ExpenseDTO()))
                .toList();
    }

    public ExpenseDTO get(final UUID id) {
        return expenseRepository.findById(id)
                .map(expense -> mapToDTO(expense, new ExpenseDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public UUID create(final ExpenseDTO expenseDTO) {
        final Expense expense = new Expense();
        mapToEntity(expenseDTO, expense);
        return expenseRepository.save(expense).getId();
    }

    public void update(final UUID id, final ExpenseDTO expenseDTO) {
        final Expense expense = expenseRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(expenseDTO, expense);
        expenseRepository.save(expense);
    }

    public void delete(final UUID id) {
        expenseRepository.deleteById(id);
    }

    private ExpenseDTO mapToDTO(final Expense expense, final ExpenseDTO expenseDTO) {
        expenseDTO.setId(expense.getId());
        expenseDTO.setDescription(expense.getDescription());
        expenseDTO.setAmount(expense.getAmount());
        expenseDTO.setDate(expense.getDate());
        expenseDTO.setPaymentMethod(expense.getPaymentMethod());
        expenseDTO.setSupplierOrClient(expense.getSupplierOrClient());
        expenseDTO.setInvoice(expense.getInvoice());
        expenseDTO.setCategory(expense.getCategory());
        expenseDTO.setChurch(expense.getChurch() == null ? null : expense.getChurch().getId());
        return expenseDTO;
    }

    private Expense mapToEntity(final ExpenseDTO expenseDTO, final Expense expense) {
        expense.setDescription(expenseDTO.getDescription());
        expense.setAmount(expenseDTO.getAmount());
        expense.setDate(expenseDTO.getDate());
        expense.setPaymentMethod(expenseDTO.getPaymentMethod());
        expense.setSupplierOrClient(expenseDTO.getSupplierOrClient());
        expense.setInvoice(expenseDTO.getInvoice());
        expense.setCategory(expenseDTO.getCategory());
        final Church church = expenseDTO.getChurch() == null ? null : churchRepository.findById(expenseDTO.getChurch())
                .orElseThrow(() -> new NotFoundException("church not found"));
        expense.setChurch(church);
        return expense;
    }

}
