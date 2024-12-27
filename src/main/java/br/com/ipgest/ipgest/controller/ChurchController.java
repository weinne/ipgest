package br.com.ipgest.ipgest.controller;

import br.com.ipgest.ipgest.model.ChurchDTO;
import br.com.ipgest.ipgest.model.ChurchStatus;
import br.com.ipgest.ipgest.service.ChurchService;
import br.com.ipgest.ipgest.util.ReferencedWarning;
import br.com.ipgest.ipgest.util.WebUtils;
import jakarta.validation.Valid;
import java.util.UUID;
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
@RequestMapping("/churches")
public class ChurchController {

    private final ChurchService churchService;

    public ChurchController(final ChurchService churchService) {
        this.churchService = churchService;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("statusValues", ChurchStatus.values());
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("churches", churchService.findAll());
        return "church/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("church") final ChurchDTO churchDTO) {
        return "church/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("church") @Valid final ChurchDTO churchDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "church/add";
        }
        churchService.create(churchDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("church.create.success"));
        return "redirect:/churches";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") final UUID id, final Model model) {
        model.addAttribute("church", churchService.get(id));
        return "church/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") final UUID id,
            @ModelAttribute("church") @Valid final ChurchDTO churchDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "church/edit";
        }
        churchService.update(id, churchDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("church.update.success"));
        return "redirect:/churches";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable(name = "id") final UUID id,
            final RedirectAttributes redirectAttributes) {
        final ReferencedWarning referencedWarning = churchService.getReferencedWarning(id);
        if (referencedWarning != null) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR,
                    WebUtils.getMessage(referencedWarning.getKey(), referencedWarning.getParams().toArray()));
        } else {
            churchService.delete(id);
            redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("church.delete.success"));
        }
        return "redirect:/churches";
    }

}
