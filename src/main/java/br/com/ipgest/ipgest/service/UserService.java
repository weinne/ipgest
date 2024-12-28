package br.com.ipgest.ipgest.service;

import br.com.ipgest.ipgest.domain.Church;
import br.com.ipgest.ipgest.domain.Task;
import br.com.ipgest.ipgest.domain.User;
import br.com.ipgest.ipgest.model.UserDTO;
import br.com.ipgest.ipgest.repos.ChurchRepository;
import br.com.ipgest.ipgest.repos.TaskRepository;
import br.com.ipgest.ipgest.repos.UserRepository;
import br.com.ipgest.ipgest.util.NotFoundException;
import br.com.ipgest.ipgest.util.ReferencedWarning;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import br.com.ipgest.ipgest.config.TenantContext;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final ChurchRepository churchRepository;
    private final TaskRepository taskRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private TenantContext tenantContext;

    public UserService(final UserRepository userRepository, final ChurchRepository churchRepository,
                       final TaskRepository taskRepository) {
        this.userRepository = userRepository;
        this.churchRepository = churchRepository;
        this.taskRepository = taskRepository;
    }

    @Transactional
    public List<UserDTO> findAll() {
        UUID tenantId = tenantContext.getCurrentTenantId();
        entityManager.unwrap(Session.class).enableFilter("tenantFilter").setParameter("tenantId", tenantId);
        final List<User> users = userRepository.findAll();
        return users.stream()
                .sorted((u1, u2) -> u1.getUsername().compareToIgnoreCase(u2.getUsername()))
                .map(user -> mapToDTO(user, new UserDTO()))
                .toList();
    }

    @Transactional
    public UserDTO get(final Long id) {
        UUID tenantId = tenantContext.getCurrentTenantId();
        entityManager.unwrap(Session.class).enableFilter("tenantFilter").setParameter("tenantId", tenantId);

        return userRepository.findById(id)
                .map(user -> mapToDTO(user, new UserDTO()))
                .orElseThrow(NotFoundException::new);
    }

    @Transactional
    public Long create(final UserDTO userDTO) {
        final User user = new User();
        mapToEntity(userDTO, user);
        if (tenantContext.getCurrentTenantId() != null) {
            user.setChurch(churchRepository.findById(tenantContext.getCurrentTenantId())
                    .orElseThrow(() -> new IllegalStateException("Church not found")));
        }
        user.setEnabled(true);
        return userRepository.save(user).getId();
    }

    @Transactional
    public void update(final Long id, final UserDTO userDTO) {
        final User user = userRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(userDTO, user);
        userRepository.save(user);
    }

    @Transactional
    public void delete(final Long id) {
        final User user = userRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        userRepository.delete(user);
    }

    private UserDTO mapToDTO(final User user, final UserDTO userDTO) {
        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setPassword(user.getPassword());
        userDTO.setEmail(user.getEmail());
        userDTO.setEnabled(user.getEnabled());
        userDTO.setChurch(user.getChurch() == null ? null : user.getChurch().getId());
        return userDTO;
    }

    private User mapToEntity(final UserDTO userDTO, final User user) {
        user.setUsername(userDTO.getUsername());
        user.setPassword(userDTO.getPassword());
        user.setEmail(userDTO.getEmail());
        user.setEnabled(userDTO.getEnabled());
        final Church church = userDTO.getChurch() == null ? null : churchRepository.findById(userDTO.getChurch())
                .orElseThrow(() -> new NotFoundException("church not found"));
        user.setChurch(church);
        return user;
    }

    public ReferencedWarning getReferencedWarning(final Long id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final User user = userRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Task userTask = taskRepository.findFirstByUser(user);
        if (userTask != null) {
            referencedWarning.setKey("user.task.user.referenced");
            referencedWarning.addParam(userTask.getId());
            return referencedWarning;
        }
        return null;
    }

    public Optional<User> getAuthenticatedUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal == null) {
            throw new IllegalStateException("No authentication found in security context");
        }

        if (principal instanceof UserDetails userDetails) {
            String username = userDetails.getUsername();
            return userRepository.findByUsername(username);
        } else {
            throw new IllegalStateException("Authenticated principal is not an instance of UserDetails");
        }
    }

    public boolean isUserInAuthenticatedUserChurch(UserDTO userDTO) {
        Optional<User> authenticatedUser = getAuthenticatedUser();
        if (authenticatedUser.isEmpty()) {
            return false;
        }

        User user = authenticatedUser.get();
        return user.getChurch() != null && user.getChurch().getId().equals(userDTO.getChurch());
    }

}
