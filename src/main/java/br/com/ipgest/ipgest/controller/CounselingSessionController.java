package br.com.ipgest.ipgest.controller;

import br.com.ipgest.ipgest.domain.Church;
import br.com.ipgest.ipgest.model.CounselingSessionDTO;
import br.com.ipgest.ipgest.repos.ChurchRepository;
import br.com.ipgest.ipgest.service.CounselingSessionService;
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
@RequestMapping("/counselingSessions")
public class CounselingSessionController {

    private final CounselingSessionService counselingSessionService;
    private final ChurchRepository churchRepository;

    public CounselingSessionController(final CounselingSessionService counselingSessionService,
            final ChurchRepository churchRepository) {
        this.counselingSessionService = counselingSessionService;
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
        model.addAttribute("counselingSessions", counselingSessionService.findAll());
        return "counselingSession/list";
    }

    @GetMapping("/add")
    public String add(
            @ModelAttribute("counselingSession") final CounselingSessionDTO counselingSessionDTO) {
        return "counselingSession/add";
    }

    @PostMapping("/add")
    public String add(
            @ModelAttribute("counselingSession") @Valid final CounselingSessionDTO counselingSessionDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "counselingSession/add";
        }
        counselingSessionService.create(counselingSessionDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("counselingSession.create.success"));
        return "redirect:/counselingSessions";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") final UUID id, final Model model) {
        model.addAttribute("counselingSession", counselingSessionService.get(id));
        return "counselingSession/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") final UUID id,
            @ModelAttribute("counselingSession") @Valid final CounselingSessionDTO counselingSessionDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "counselingSession/edit";
        }
        counselingSessionService.update(id, counselingSessionDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("counselingSession.update.success"));
        return "redirect:/counselingSessions";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable(name = "id") final UUID id,
            final RedirectAttributes redirectAttributes) {
        counselingSessionService.delete(id);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("counselingSession.delete.success"));
        return "redirect:/counselingSessions";
    }

}
