package br.com.ipgest.ipgest.controller;

import br.com.ipgest.ipgest.domain.Church;
import br.com.ipgest.ipgest.domain.Hymn;
import br.com.ipgest.ipgest.model.WorshipServiceDTO;
import br.com.ipgest.ipgest.repos.ChurchRepository;
import br.com.ipgest.ipgest.repos.HymnRepository;
import br.com.ipgest.ipgest.service.WorshipServiceService;
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
@RequestMapping("/worshipServices")
public class WorshipServiceController {

    private final WorshipServiceService worshipServiceService;
    private final ChurchRepository churchRepository;
    private final HymnRepository hymnRepository;

    public WorshipServiceController(final WorshipServiceService worshipServiceService,
            final ChurchRepository churchRepository, final HymnRepository hymnRepository) {
        this.worshipServiceService = worshipServiceService;
        this.churchRepository = churchRepository;
        this.hymnRepository = hymnRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("churchValues", churchRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Church::getId, Church::getCnpj)));
        model.addAttribute("hymnsValues", hymnRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Hymn::getId, Hymn::getTitle)));
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("worshipServices", worshipServiceService.findAll());
        return "worshipService/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("worshipService") final WorshipServiceDTO worshipServiceDTO) {
        return "worshipService/add";
    }

    @PostMapping("/add")
    public String add(
            @ModelAttribute("worshipService") @Valid final WorshipServiceDTO worshipServiceDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "worshipService/add";
        }
        worshipServiceService.create(worshipServiceDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("worshipService.create.success"));
        return "redirect:/worshipServices";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") final UUID id, final Model model) {
        model.addAttribute("worshipService", worshipServiceService.get(id));
        return "worshipService/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") final UUID id,
            @ModelAttribute("worshipService") @Valid final WorshipServiceDTO worshipServiceDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "worshipService/edit";
        }
        worshipServiceService.update(id, worshipServiceDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("worshipService.update.success"));
        return "redirect:/worshipServices";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable(name = "id") final UUID id,
            final RedirectAttributes redirectAttributes) {
        worshipServiceService.delete(id);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("worshipService.delete.success"));
        return "redirect:/worshipServices";
    }

}
