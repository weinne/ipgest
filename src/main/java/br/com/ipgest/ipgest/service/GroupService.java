package br.com.ipgest.ipgest.service;

import br.com.ipgest.ipgest.domain.Church;
import br.com.ipgest.ipgest.domain.Group;
import br.com.ipgest.ipgest.model.GroupDTO;
import br.com.ipgest.ipgest.repos.ChurchRepository;
import br.com.ipgest.ipgest.repos.GroupRepository;
import br.com.ipgest.ipgest.repos.MemberRepository;
import br.com.ipgest.ipgest.util.NotFoundException;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
@Transactional
public class GroupService {

    private final GroupRepository groupRepository;
    private final ChurchRepository churchRepository;
    private final MemberRepository memberRepository;

    public GroupService(final GroupRepository groupRepository,
            final ChurchRepository churchRepository, final MemberRepository memberRepository) {
        this.groupRepository = groupRepository;
        this.churchRepository = churchRepository;
        this.memberRepository = memberRepository;
    }

    public List<GroupDTO> findAll() {
        final List<Group> groups = groupRepository.findAll(Sort.by("id"));
        return groups.stream()
                .map(group -> mapToDTO(group, new GroupDTO()))
                .toList();
    }

    public GroupDTO get(final Long id) {
        return groupRepository.findById(id)
                .map(group -> mapToDTO(group, new GroupDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final GroupDTO groupDTO) {
        final Group group = new Group();
        mapToEntity(groupDTO, group);
        return groupRepository.save(group).getId();
    }

    public void update(final Long id, final GroupDTO groupDTO) {
        final Group group = groupRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(groupDTO, group);
        groupRepository.save(group);
    }

    public void delete(final Long id) {
        final Group group = groupRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        // remove many-to-many relations at owning side
        memberRepository.findAllByGroups(group)
                .forEach(member -> member.getGroups().remove(group));
        groupRepository.delete(group);
    }

    private GroupDTO mapToDTO(final Group group, final GroupDTO groupDTO) {
        groupDTO.setId(group.getId());
        groupDTO.setName(group.getName());
        groupDTO.setDescription(group.getDescription());
        groupDTO.setEstablishedDate(group.getEstablishedDate());
        groupDTO.setType(group.getType());
        groupDTO.setChurch(group.getChurch() == null ? null : group.getChurch().getId());
        return groupDTO;
    }

    private Group mapToEntity(final GroupDTO groupDTO, final Group group) {
        group.setName(groupDTO.getName());
        group.setDescription(groupDTO.getDescription());
        group.setEstablishedDate(groupDTO.getEstablishedDate());
        group.setType(groupDTO.getType());
        final Church church = groupDTO.getChurch() == null ? null : churchRepository.findById(groupDTO.getChurch())
                .orElseThrow(() -> new NotFoundException("church not found"));
        group.setChurch(church);
        return group;
    }

}
