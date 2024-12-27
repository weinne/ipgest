package br.com.ipgest.ipgest.controller;

import br.com.ipgest.ipgest.domain.Church;
import br.com.ipgest.ipgest.domain.Plan;
import br.com.ipgest.ipgest.model.PaymentProvider;
import br.com.ipgest.ipgest.model.PaymentStatus;
import br.com.ipgest.ipgest.model.SubscriptionDTO;
import br.com.ipgest.ipgest.model.SubscriptionStatus;
import br.com.ipgest.ipgest.repos.ChurchRepository;
import br.com.ipgest.ipgest.repos.PlanRepository;
import br.com.ipgest.ipgest.service.SubscriptionService;
import br.com.ipgest.ipgest.util.CustomCollectors;
import br.com.ipgest.ipgest.util.WebUtils;
import jakarta.validation.Valid;
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
@RequestMapping("/subscriptions")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;
    private final PlanRepository planRepository;
    private final ChurchRepository churchRepository;

    public SubscriptionController(final SubscriptionService subscriptionService,
            final PlanRepository planRepository, final ChurchRepository churchRepository) {
        this.subscriptionService = subscriptionService;
        this.planRepository = planRepository;
        this.churchRepository = churchRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("statusValues", SubscriptionStatus.values());
        model.addAttribute("paymentProviderValues", PaymentProvider.values());
        model.addAttribute("paymentStatusValues", PaymentStatus.values());
        model.addAttribute("planValues", planRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Plan::getId, Plan::getName)));
        model.addAttribute("churchValues", churchRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Church::getId, Church::getCnpj)));
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("subscriptions", subscriptionService.findAll());
        return "subscription/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("subscription") final SubscriptionDTO subscriptionDTO) {
        return "subscription/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("subscription") @Valid final SubscriptionDTO subscriptionDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "subscription/add";
        }
        subscriptionService.create(subscriptionDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("subscription.create.success"));
        return "redirect:/subscriptions";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") final Long id, final Model model) {
        model.addAttribute("subscription", subscriptionService.get(id));
        return "subscription/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") final Long id,
            @ModelAttribute("subscription") @Valid final SubscriptionDTO subscriptionDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "subscription/edit";
        }
        subscriptionService.update(id, subscriptionDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("subscription.update.success"));
        return "redirect:/subscriptions";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable(name = "id") final Long id,
            final RedirectAttributes redirectAttributes) {
        subscriptionService.delete(id);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("subscription.delete.success"));
        return "redirect:/subscriptions";
    }

}
