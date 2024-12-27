package br.com.ipgest.ipgest.service;

import br.com.ipgest.ipgest.config.TenantContext;
import br.com.ipgest.ipgest.domain.Church;
import br.com.ipgest.ipgest.domain.Group;
import br.com.ipgest.ipgest.domain.Member;
import br.com.ipgest.ipgest.model.MemberDTO;
import br.com.ipgest.ipgest.repos.ChurchRepository;
import br.com.ipgest.ipgest.repos.GroupRepository;
import br.com.ipgest.ipgest.repos.MemberRepository;
import br.com.ipgest.ipgest.repos.TaskRepository;
import br.com.ipgest.ipgest.util.NotFoundException;
import jakarta.transaction.Transactional;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;


@Service
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;
    private final ChurchRepository churchRepository;
    private final GroupRepository groupRepository;
    private final TaskRepository taskRepository;
    private final TenantContext tenantContext;

    public MemberService(final MemberRepository memberRepository,
            final ChurchRepository churchRepository, final GroupRepository groupRepository,
            final TaskRepository taskRepository, final TenantContext tenantContext) {
        this.memberRepository = memberRepository;
        this.churchRepository = churchRepository;
        this.groupRepository = groupRepository;
        this.taskRepository = taskRepository;
        this.tenantContext = tenantContext;
    }

    public List<MemberDTO> findAll() {
        UUID tenantId = tenantContext.getCurrentTenantId();
        final List<Member> members = memberRepository.findByChurchId(tenantId);
        return members.stream()
                .sorted((m1, m2) -> m1.getFullName().compareToIgnoreCase(m2.getFullName()))
                .map(member -> mapToDTO(member, new MemberDTO()))
                .toList();
    }

    public MemberDTO get(final UUID id) {
        UUID tenantId = tenantContext.getCurrentTenantId();
        return memberRepository.findById(id)
                .filter(member -> member.getChurch() != null && member.getChurch().getId().equals(tenantId))
                .map(member -> mapToDTO(member, new MemberDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public UUID create(MemberDTO memberDTO) {
        UUID tenantId = tenantContext.getCurrentTenantId();
        final Church church = churchRepository.findById(tenantId)
                .orElseThrow(() -> new NotFoundException("Church not found"));
        final Member member = new Member();
        mapToEntity(memberDTO, member);
        member.setChurch(church);
        return memberRepository.save(member).getId();
    }

    public void update(final UUID id, final MemberDTO memberDTO) {
        UUID tenantId = tenantContext.getCurrentTenantId();
        final Member member = memberRepository.findById(id)
                .filter(m -> m.getChurch() != null && m.getChurch().getId().equals(tenantId))
                .orElseThrow(NotFoundException::new);
        mapToEntity(memberDTO, member);
        memberRepository.save(member);
    }

    public void delete(final UUID id) {
        UUID tenantId = tenantContext.getCurrentTenantId();
        final Member member = memberRepository.findById(id)
                .filter(m -> m.getChurch() != null && m.getChurch().getId().equals(tenantId))
                .orElseThrow(NotFoundException::new);
        // remove many-to-many relations at owning side
        taskRepository.findAllByMembers(member)
                .forEach(task -> task.getMembers().remove(member));
        memberRepository.delete(member);
    }

    private MemberDTO mapToDTO(final Member member, final MemberDTO memberDTO) {
        memberDTO.setId(member.getId());
        memberDTO.setFullName(member.getFullName());
        memberDTO.setPhone(member.getPhone());
        memberDTO.setEmail(member.getEmail());
        memberDTO.setAddress(member.getAddress());
        memberDTO.setCity(member.getCity());
        memberDTO.setPostalCode(member.getPostalCode());
        memberDTO.setBirthCity(member.getBirthCity());
        memberDTO.setState(member.getState());
        memberDTO.setRegistrationNumber(member.getRegistrationNumber());
        memberDTO.setCpf(member.getCpf());
        memberDTO.setBirthDate(member.getBirthDate());
        memberDTO.setGender(member.getGender());
        memberDTO.setMaritalStatus(member.getMaritalStatus());
        memberDTO.setDateOfDeath(member.getDateOfDeath());
        memberDTO.setPreviousReligion(member.getPreviousReligion());
        memberDTO.setStatus(member.getStatus());
        memberDTO.setAdmissionDate(member.getAdmissionDate());
        memberDTO.setAdmissionMode(member.getAdmissionMode());
        memberDTO.setExitDate(member.getExitDate());
        memberDTO.setExitReason(member.getExitReason());
        memberDTO.setBaptismDate(member.getBaptismDate());
        memberDTO.setProfissionDate(member.getProfissionDate());
        memberDTO.setTransferDate(member.getTransferDate());
        memberDTO.setNotes(member.getNotes());
        memberDTO.setChurch(member.getChurch() == null ? null : member.getChurch().getId());
        memberDTO.setGroups(member.getGroups().stream()
                .map(group -> group.getId())
                .toList());
        return memberDTO;
    }

    private Member mapToEntity(final MemberDTO memberDTO, final Member member) {
        member.setFullName(memberDTO.getFullName());
        member.setPhone(memberDTO.getPhone());
        member.setEmail(memberDTO.getEmail());
        member.setAddress(memberDTO.getAddress());
        member.setCity(memberDTO.getCity());
        member.setPostalCode(memberDTO.getPostalCode());
        member.setBirthCity(memberDTO.getBirthCity());
        member.setState(memberDTO.getState());
        member.setRegistrationNumber(memberDTO.getRegistrationNumber());
        member.setCpf(memberDTO.getCpf());
        member.setBirthDate(memberDTO.getBirthDate());
        member.setGender(memberDTO.getGender());
        member.setMaritalStatus(memberDTO.getMaritalStatus());
        member.setDateOfDeath(memberDTO.getDateOfDeath());
        member.setPreviousReligion(memberDTO.getPreviousReligion());
        member.setStatus(memberDTO.getStatus());
        member.setAdmissionDate(memberDTO.getAdmissionDate());
        member.setAdmissionMode(memberDTO.getAdmissionMode());
        member.setExitDate(memberDTO.getExitDate());
        member.setExitReason(memberDTO.getExitReason());
        member.setBaptismDate(memberDTO.getBaptismDate());
        member.setProfissionDate(memberDTO.getProfissionDate());
        member.setTransferDate(memberDTO.getTransferDate());
        member.setNotes(memberDTO.getNotes());
        final Church church = memberDTO.getChurch() == null ? null : churchRepository.findById(memberDTO.getChurch())
                .orElseThrow(() -> new NotFoundException("church not found"));
        member.setChurch(church);
        final List<Group> groups = groupRepository.findAllById(
                memberDTO.getGroups() == null ? Collections.emptyList() : memberDTO.getGroups());
        if (groups.size() != (memberDTO.getGroups() == null ? 0 : memberDTO.getGroups().size())) {
            throw new NotFoundException("one of groups not found");
        }
        member.setGroups(new HashSet<>(groups));
        return member;
    }

}
