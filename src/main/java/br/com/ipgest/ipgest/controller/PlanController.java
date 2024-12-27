package br.com.ipgest.ipgest.controller;

import br.com.ipgest.ipgest.model.PlanDTO;
import br.com.ipgest.ipgest.service.PlanService;
import br.com.ipgest.ipgest.util.ReferencedWarning;
import br.com.ipgest.ipgest.util.WebUtils;
import jakarta.validation.Valid;
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
@RequestMapping("/plans")
public class PlanController {

    private final PlanService planService;

    public PlanController(final PlanService planService) {
        this.planService = planService;
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("plans", planService.findAll());
        return "plan/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("plan") final PlanDTO planDTO) {
        return "plan/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("plan") @Valid final PlanDTO planDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "plan/add";
        }
        planService.create(planDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("plan.create.success"));
        return "redirect:/plans";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") final Long id, final Model model) {
        model.addAttribute("plan", planService.get(id));
        return "plan/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") final Long id,
            @ModelAttribute("plan") @Valid final PlanDTO planDTO, final BindingResult bindingResult,
            final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "plan/edit";
        }
        planService.update(id, planDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("plan.update.success"));
        return "redirect:/plans";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable(name = "id") final Long id,
            final RedirectAttributes redirectAttributes) {
        final ReferencedWarning referencedWarning = planService.getReferencedWarning(id);
        if (referencedWarning != null) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR,
                    WebUtils.getMessage(referencedWarning.getKey(), referencedWarning.getParams().toArray()));
        } else {
            planService.delete(id);
            redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("plan.delete.success"));
        }
        return "redirect:/plans";
    }

}
