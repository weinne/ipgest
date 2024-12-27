package br.com.ipgest.ipgest.controller;

import br.com.ipgest.ipgest.domain.Church;
import br.com.ipgest.ipgest.model.IncomeDTO;
import br.com.ipgest.ipgest.repos.ChurchRepository;
import br.com.ipgest.ipgest.service.IncomeService;
import br.com.ipgest.ipgest.util.CustomCollectors;
import br.com.ipgest.ipgest.util.WebUtils;
import jakarta.validation.Valid;
import java.util.UUID;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/incomes")
public class IncomeController {

    private final IncomeService incomeService;
    private final ChurchRepository churchRepository;

    public IncomeController(final IncomeService incomeService,
            final ChurchRepository churchRepository) {
        this.incomeService = incomeService;
        this.churchRepository = churchRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("churchValues", churchRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Church::getId, Church::getCnpj)));
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("incomes", incomeService.findAll());
        return "income/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("income") final IncomeDTO incomeDTO) {
        return "income/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("income") @Valid final IncomeDTO incomeDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "income/add";
        }
        incomeService.create(incomeDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("income.create.success"));
        return "redirect:/incomes";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") final UUID id, final Model model) {
        model.addAttribute("income", incomeService.get(id));
        return "income/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") final UUID id,
            @ModelAttribute("income") @Valid final IncomeDTO incomeDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "income/edit";
        }
        incomeService.update(id, incomeDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("income.update.success"));
        return "redirect:/incomes";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable(name = "id") final UUID id,
            final RedirectAttributes redirectAttributes) {
        incomeService.delete(id);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("income.delete.success"));
        return "redirect:/incomes";
    }

}
