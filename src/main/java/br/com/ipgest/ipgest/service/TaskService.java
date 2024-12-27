package br.com.ipgest.ipgest.service;

import br.com.ipgest.ipgest.domain.Church;
import br.com.ipgest.ipgest.domain.Member;
import br.com.ipgest.ipgest.domain.Task;
import br.com.ipgest.ipgest.domain.User;
import br.com.ipgest.ipgest.model.TaskDTO;
import br.com.ipgest.ipgest.repos.ChurchRepository;
import br.com.ipgest.ipgest.repos.MemberRepository;
import br.com.ipgest.ipgest.repos.TaskRepository;
import br.com.ipgest.ipgest.repos.UserRepository;
import br.com.ipgest.ipgest.util.NotFoundException;
import jakarta.transaction.Transactional;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
@Transactional
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final MemberRepository memberRepository;
    private final ChurchRepository churchRepository;

    public TaskService(final TaskRepository taskRepository, final UserRepository userRepository,
            final MemberRepository memberRepository, final ChurchRepository churchRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.memberRepository = memberRepository;
        this.churchRepository = churchRepository;
    }

    public List<TaskDTO> findAll() {
        final List<Task> tasks = taskRepository.findAll(Sort.by("id"));
        return tasks.stream()
                .map(task -> mapToDTO(task, new TaskDTO()))
                .toList();
    }

    public TaskDTO get(final Long id) {
        return taskRepository.findById(id)
                .map(task -> mapToDTO(task, new TaskDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final TaskDTO taskDTO) {
        final Task task = new Task();
        mapToEntity(taskDTO, task);
        return taskRepository.save(task).getId();
    }

    public void update(final Long id, final TaskDTO taskDTO) {
        final Task task = taskRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(taskDTO, task);
        taskRepository.save(task);
    }

    public void delete(final Long id) {
        taskRepository.deleteById(id);
    }

    private TaskDTO mapToDTO(final Task task, final TaskDTO taskDTO) {
        taskDTO.setId(task.getId());
        taskDTO.setTitle(task.getTitle());
        taskDTO.setDescription(task.getDescription());
        taskDTO.setDueDate(task.getDueDate());
        taskDTO.setStatus(task.getStatus());
        taskDTO.setPriority(task.getPriority());
        taskDTO.setNotes(task.getNotes());
        taskDTO.setUser(task.getUser() == null ? null : task.getUser().getId());
        taskDTO.setMembers(task.getMembers().stream()
                .map(member -> member.getId())
                .toList());
        taskDTO.setChurch(task.getChurch() == null ? null : task.getChurch().getId());
        return taskDTO;
    }

    private Task mapToEntity(final TaskDTO taskDTO, final Task task) {
        task.setTitle(taskDTO.getTitle());
        task.setDescription(taskDTO.getDescription());
        task.setDueDate(taskDTO.getDueDate());
        task.setStatus(taskDTO.getStatus());
        task.setPriority(taskDTO.getPriority());
        task.setNotes(taskDTO.getNotes());
        final User user = taskDTO.getUser() == null ? null : userRepository.findById(taskDTO.getUser())
                .orElseThrow(() -> new NotFoundException("user not found"));
        task.setUser(user);
        final List<Member> members = memberRepository.findAllById(
                taskDTO.getMembers() == null ? Collections.emptyList() : taskDTO.getMembers());
        if (members.size() != (taskDTO.getMembers() == null ? 0 : taskDTO.getMembers().size())) {
            throw new NotFoundException("one of members not found");
        }
        task.setMembers(new HashSet<>(members));
        final Church church = taskDTO.getChurch() == null ? null : churchRepository.findById(taskDTO.getChurch())
                .orElseThrow(() -> new NotFoundException("church not found"));
        task.setChurch(church);
        return task;
    }

}
