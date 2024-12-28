package br.com.ipgest.ipgest.controller;

import br.com.ipgest.ipgest.domain.Church;
import br.com.ipgest.ipgest.model.UserDTO;
import br.com.ipgest.ipgest.repos.ChurchRepository;
import br.com.ipgest.ipgest.service.UserService;
import br.com.ipgest.ipgest.util.CustomCollectors;
import br.com.ipgest.ipgest.util.ReferencedWarning;
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
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final ChurchRepository churchRepository;

    public UserController(final UserService userService, final ChurchRepository churchRepository) {
        this.userService = userService;
        this.churchRepository = churchRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("churchValues", churchRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Church::getId, Church::getName)));
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("users", userService.findAll());
        return "user/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("user") final UserDTO userDTO) {
        return "user/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("user") @Valid final UserDTO userDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "user/add";
        }
        userService.create(userDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("user.create.success"));
        return "redirect:/users";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") final Long id, final Model model) {

        UserDTO userDTO = userService.get(id);
        if (!userService.isUserInAuthenticatedUserChurch(userDTO)) {
            return "redirect:/users";
        }

        model.addAttribute("user", userService.get(id));
        return "user/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") final Long id,
            @ModelAttribute("user") @Valid final UserDTO userDTO, final BindingResult bindingResult,
            final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "user/edit";
        }

        if (!userService.isUserInAuthenticatedUserChurch(userDTO)) {
            return "redirect:/users";
        }

        userService.update(id, userDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("user.update.success"));
        return "redirect:/users";
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable(name = "id") final Long id, @Valid final UserDTO userDTO, 
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "user/edit";
        }

        if (!userService.isUserInAuthenticatedUserChurch(userDTO)) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR, WebUtils.getMessage("user.not.authorized"));
            return "redirect:/users";
        }

        userService.update(id, userDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("user.update.success"));
        return "redirect:/users";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable(name = "id") final Long id, final RedirectAttributes redirectAttributes) {
        final ReferencedWarning referencedWarning = userService.getReferencedWarning(id);
        if (referencedWarning != null) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR,
                    WebUtils.getMessage(referencedWarning.getKey(), referencedWarning.getParams().toArray()));
        } else {
            UserDTO userDTO = userService.get(id);
            if (!userService.isUserInAuthenticatedUserChurch(userDTO)) {
                redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR, WebUtils.getMessage("user.not.authorized"));
                return "redirect:/users";
            }

            userService.delete(id);
            redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("user.delete.success"));
        }
        return "redirect:/users";
    }

}
