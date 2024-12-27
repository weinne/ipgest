package br.com.ipgest.ipgest.service;

import br.com.ipgest.ipgest.domain.Church;
import br.com.ipgest.ipgest.domain.Income;
import br.com.ipgest.ipgest.model.IncomeDTO;
import br.com.ipgest.ipgest.repos.ChurchRepository;
import br.com.ipgest.ipgest.repos.IncomeRepository;
import br.com.ipgest.ipgest.util.NotFoundException;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class IncomeService {

    private final IncomeRepository incomeRepository;
    private final ChurchRepository churchRepository;

    public IncomeService(final IncomeRepository incomeRepository,
            final ChurchRepository churchRepository) {
        this.incomeRepository = incomeRepository;
        this.churchRepository = churchRepository;
    }

    public List<IncomeDTO> findAll() {
        final List<Income> incomes = incomeRepository.findAll(Sort.by("id"));
        return incomes.stream()
                .map(income -> mapToDTO(income, new IncomeDTO()))
                .toList();
    }

    public IncomeDTO get(final UUID id) {
        return incomeRepository.findById(id)
                .map(income -> mapToDTO(income, new IncomeDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public UUID create(final IncomeDTO incomeDTO) {
        final Income income = new Income();
        mapToEntity(incomeDTO, income);
        return incomeRepository.save(income).getId();
    }

    public void update(final UUID id, final IncomeDTO incomeDTO) {
        final Income income = incomeRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(incomeDTO, income);
        incomeRepository.save(income);
    }

    public void delete(final UUID id) {
        incomeRepository.deleteById(id);
    }

    private IncomeDTO mapToDTO(final Income income, final IncomeDTO incomeDTO) {
        incomeDTO.setId(income.getId());
        incomeDTO.setDescription(income.getDescription());
        incomeDTO.setAmount(income.getAmount());
        incomeDTO.setDate(income.getDate());
        incomeDTO.setPaymentMethod(income.getPaymentMethod());
        incomeDTO.setSupplierOrClient(income.getSupplierOrClient());
        incomeDTO.setInvoice(income.getInvoice());
        incomeDTO.setType(income.getType());
        incomeDTO.setChurch(income.getChurch() == null ? null : income.getChurch().getId());
        return incomeDTO;
    }

    private Income mapToEntity(final IncomeDTO incomeDTO, final Income income) {
        income.setDescription(incomeDTO.getDescription());
        income.setAmount(incomeDTO.getAmount());
        income.setDate(incomeDTO.getDate());
        income.setPaymentMethod(incomeDTO.getPaymentMethod());
        income.setSupplierOrClient(incomeDTO.getSupplierOrClient());
        income.setInvoice(incomeDTO.getInvoice());
        income.setType(incomeDTO.getType());
        final Church church = incomeDTO.getChurch() == null ? null : churchRepository.findById(incomeDTO.getChurch())
                .orElseThrow(() -> new NotFoundException("church not found"));
        income.setChurch(church);
        return income;
    }

}
