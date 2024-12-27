package br.com.ipgest.ipgest.controller;

import br.com.ipgest.ipgest.domain.Church;
import br.com.ipgest.ipgest.domain.Group;
import br.com.ipgest.ipgest.model.AdmissionMode;
import br.com.ipgest.ipgest.model.ExitReason;
import br.com.ipgest.ipgest.model.Gender;
import br.com.ipgest.ipgest.model.MaritalStatus;
import br.com.ipgest.ipgest.model.MemberDTO;
import br.com.ipgest.ipgest.model.MemberStatus;
import br.com.ipgest.ipgest.repos.ChurchRepository;
import br.com.ipgest.ipgest.repos.GroupRepository;
import br.com.ipgest.ipgest.service.MemberService;
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
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;
    private final ChurchRepository churchRepository;
    private final GroupRepository groupRepository;

    public MemberController(final MemberService memberService,
            final ChurchRepository churchRepository, final GroupRepository groupRepository) {
        this.memberService = memberService;
        this.churchRepository = churchRepository;
        this.groupRepository = groupRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("genderValues", Gender.values());
        model.addAttribute("maritalStatusValues", MaritalStatus.values());
        model.addAttribute("statusValues", MemberStatus.values());
        model.addAttribute("admissionModeValues", AdmissionMode.values());
        model.addAttribute("exitReasonValues", ExitReason.values());
        model.addAttribute("churchValues", churchRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Church::getId, Church::getCnpj)));
        model.addAttribute("groupsValues", groupRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Group::getId, Group::getName)));
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("members", memberService.findAll());
        return "member/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("member") final MemberDTO memberDTO) {
        return "member/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("member") @Valid final MemberDTO memberDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "member/add";
        }
        memberService.create(memberDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("member.create.success"));
        return "redirect:/members";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") final UUID id, final Model model) {
        model.addAttribute("member", memberService.get(id));
        return "member/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") final UUID id,
            @ModelAttribute("member") @Valid final MemberDTO memberDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "member/edit";
        }
        memberService.update(id, memberDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("member.update.success"));
        return "redirect:/members";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable(name = "id") final UUID id,
            final RedirectAttributes redirectAttributes) {
        memberService.delete(id);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("member.delete.success"));
        return "redirect:/members";
    }

}
