package br.com.ipgest.ipgest.controller;

import br.com.ipgest.ipgest.domain.Church;
import br.com.ipgest.ipgest.model.AppointmentDTO;
import br.com.ipgest.ipgest.model.AppointmentType;
import br.com.ipgest.ipgest.repos.ChurchRepository;
import br.com.ipgest.ipgest.service.AppointmentService;
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
@RequestMapping("/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final ChurchRepository churchRepository;

    public AppointmentController(final AppointmentService appointmentService,
            final ChurchRepository churchRepository) {
        this.appointmentService = appointmentService;
        this.churchRepository = churchRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("typeValues", AppointmentType.values());
        model.addAttribute("churchValues", churchRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Church::getId, Church::getCnpj)));
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("appointments", appointmentService.findAll());
        return "appointment/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("appointment") final AppointmentDTO appointmentDTO) {
        return "appointment/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("appointment") @Valid final AppointmentDTO appointmentDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "appointment/add";
        }
        appointmentService.create(appointmentDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("appointment.create.success"));
        return "redirect:/appointments";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") final UUID id, final Model model) {
        model.addAttribute("appointment", appointmentService.get(id));
        return "appointment/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") final UUID id,
            @ModelAttribute("appointment") @Valid final AppointmentDTO appointmentDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "appointment/edit";
        }
        appointmentService.update(id, appointmentDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("appointment.update.success"));
        return "redirect:/appointments";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable(name = "id") final UUID id,
            final RedirectAttributes redirectAttributes) {
        appointmentService.delete(id);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("appointment.delete.success"));
        return "redirect:/appointments";
    }

}
