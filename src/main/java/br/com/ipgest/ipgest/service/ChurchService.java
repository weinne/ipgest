package br.com.ipgest.ipgest.service;

import br.com.ipgest.ipgest.domain.Appointment;
import br.com.ipgest.ipgest.domain.Church;
import br.com.ipgest.ipgest.domain.CounselingSession;
import br.com.ipgest.ipgest.domain.Expense;
import br.com.ipgest.ipgest.domain.Group;
import br.com.ipgest.ipgest.domain.Hymn;
import br.com.ipgest.ipgest.domain.Income;
import br.com.ipgest.ipgest.domain.Member;
import br.com.ipgest.ipgest.domain.Subscription;
import br.com.ipgest.ipgest.domain.Task;
import br.com.ipgest.ipgest.domain.User;
import br.com.ipgest.ipgest.domain.WorshipService;
import br.com.ipgest.ipgest.model.ChurchDTO;
import br.com.ipgest.ipgest.repos.AppointmentRepository;
import br.com.ipgest.ipgest.repos.ChurchRepository;
import br.com.ipgest.ipgest.repos.CounselingSessionRepository;
import br.com.ipgest.ipgest.repos.ExpenseRepository;
import br.com.ipgest.ipgest.repos.GroupRepository;
import br.com.ipgest.ipgest.repos.HymnRepository;
import br.com.ipgest.ipgest.repos.IncomeRepository;
import br.com.ipgest.ipgest.repos.MemberRepository;
import br.com.ipgest.ipgest.repos.SubscriptionRepository;
import br.com.ipgest.ipgest.repos.TaskRepository;
import br.com.ipgest.ipgest.repos.UserRepository;
import br.com.ipgest.ipgest.repos.WorshipServiceRepository;
import br.com.ipgest.ipgest.util.NotFoundException;
import br.com.ipgest.ipgest.util.ReferencedWarning;
import br.com.ipgest.ipgest.config.TenantContext;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import br.com.ipgest.ipgest.model.ChurchStatus;


@Service
public class ChurchService {

    private final ChurchRepository churchRepository;
    private final UserRepository userRepository;
    private final MemberRepository memberRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final GroupRepository groupRepository;
    private final TaskRepository taskRepository;
    private final AppointmentRepository appointmentRepository;
    private final CounselingSessionRepository counselingSessionRepository;
    private final WorshipServiceRepository worshipServiceRepository;
    private final HymnRepository hymnRepository;
    private final ExpenseRepository expenseRepository;
    private final IncomeRepository incomeRepository;
    private final TenantContext tenantContext;

    public ChurchService(final ChurchRepository churchRepository,
            final UserRepository userRepository, final MemberRepository memberRepository,
            final SubscriptionRepository subscriptionRepository,
            final GroupRepository groupRepository, final TaskRepository taskRepository,
            final AppointmentRepository appointmentRepository,
            final CounselingSessionRepository counselingSessionRepository,
            final WorshipServiceRepository worshipServiceRepository,
            final HymnRepository hymnRepository, final ExpenseRepository expenseRepository,
            final IncomeRepository incomeRepository, final TenantContext tenantContext) {
        this.churchRepository = churchRepository;
        this.userRepository = userRepository;
        this.memberRepository = memberRepository;
        this.subscriptionRepository = subscriptionRepository;
        this.groupRepository = groupRepository;
        this.taskRepository = taskRepository;
        this.appointmentRepository = appointmentRepository;
        this.counselingSessionRepository = counselingSessionRepository;
        this.worshipServiceRepository = worshipServiceRepository;
        this.hymnRepository = hymnRepository;
        this.expenseRepository = expenseRepository;
        this.incomeRepository = incomeRepository;
        this.tenantContext = tenantContext;
    }

    public List<ChurchDTO> findAll() {
        UUID tenantId = tenantContext.getCurrentTenantId();
        final List<Church> churches = churchRepository.findAllById(List.of(tenantId));
        return churches.stream()
                .sorted((c1, c2) -> c1.getName().compareToIgnoreCase(c2.getName()))
                .map(church -> mapToDTO(church, new ChurchDTO()))
                .toList();
    }

    public ChurchDTO get(final UUID id) {
        UUID tenantId = tenantContext.getCurrentTenantId();
        return churchRepository.findById(id)
                .filter(church -> church.getId().equals(tenantId))
                .map(church -> mapToDTO(church, new ChurchDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public UUID create(final ChurchDTO churchDTO) {
        final Church church = new Church();
        mapToEntity(churchDTO, church);
        return churchRepository.save(church).getId();
    }

    public void update(final UUID id, final ChurchDTO churchDTO) {
        UUID tenantId = tenantContext.getCurrentTenantId();
        final Church church = churchRepository.findById(id)
                .filter(c -> c.getId().equals(tenantId))
                .orElseThrow(NotFoundException::new);
        mapToEntity(churchDTO, church);
        churchRepository.save(church);
    }

    public void delete(final UUID id) {
        UUID tenantId = tenantContext.getCurrentTenantId();
        final Church church = churchRepository.findById(id)
                .filter(c -> c.getId().equals(tenantId))
                .orElseThrow(NotFoundException::new);
        church.setStatus(ChurchStatus.INATIVA);
        churchRepository.save(church);
    }

    private ChurchDTO mapToDTO(final Church church, final ChurchDTO churchDTO) {
        churchDTO.setId(church.getId());
        churchDTO.setCnpj(church.getCnpj());
        churchDTO.setName(church.getName());
        churchDTO.setAbbreviation(church.getAbbreviation());
        churchDTO.setPresbitery(church.getPresbitery());
        churchDTO.setCity(church.getCity());
        churchDTO.setState(church.getState());
        churchDTO.setAddress(church.getAddress());
        churchDTO.setStatus(church.getStatus());
        churchDTO.setAbout(church.getAbout());
        return churchDTO;
    }

    private Church mapToEntity(final ChurchDTO churchDTO, final Church church) {
        church.setCnpj(churchDTO.getCnpj());
        church.setName(churchDTO.getName());
        church.setAbbreviation(churchDTO.getAbbreviation());
        church.setPresbitery(churchDTO.getPresbitery());
        church.setCity(churchDTO.getCity());
        church.setState(churchDTO.getState());
        church.setAddress(churchDTO.getAddress());
        church.setStatus(churchDTO.getStatus());
        church.setAbout(churchDTO.getAbout());
        return church;
    }

    public ReferencedWarning getReferencedWarning(final UUID id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Church church = churchRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final User churchUser = userRepository.findFirstByChurch(church);
        if (churchUser != null) {
            referencedWarning.setKey("church.user.church.referenced");
            referencedWarning.addParam(churchUser.getId());
            return referencedWarning;
        }
        final Member churchMember = memberRepository.findFirstByChurch(church);
        if (churchMember != null) {
            referencedWarning.setKey("church.member.church.referenced");
            referencedWarning.addParam(churchMember.getId());
            return referencedWarning;
        }
        final Subscription churchSubscription = subscriptionRepository.findFirstByChurch(church);
        if (churchSubscription != null) {
            referencedWarning.setKey("church.subscription.church.referenced");
            referencedWarning.addParam(churchSubscription.getId());
            return referencedWarning;
        }
        final Group churchGroup = groupRepository.findFirstByChurch(church);
        if (churchGroup != null) {
            referencedWarning.setKey("church.group.church.referenced");
            referencedWarning.addParam(churchGroup.getId());
            return referencedWarning;
        }
        final Task churchTask = taskRepository.findFirstByChurch(church);
        if (churchTask != null) {
            referencedWarning.setKey("church.task.church.referenced");
            referencedWarning.addParam(churchTask.getId());
            return referencedWarning;
        }
        final Appointment churchAppointment = appointmentRepository.findFirstByChurch(church);
        if (churchAppointment != null) {
            referencedWarning.setKey("church.event.church.referenced");
            referencedWarning.addParam(churchAppointment.getId());
            return referencedWarning;
        }
        final CounselingSession churchCounselingSession = counselingSessionRepository.findFirstByChurch(church);
        if (churchCounselingSession != null) {
            referencedWarning.setKey("church.event.church.referenced");
            referencedWarning.addParam(churchCounselingSession.getId());
            return referencedWarning;
        }
        final WorshipService churchWorshipService = worshipServiceRepository.findFirstByChurch(church);
        if (churchWorshipService != null) {
            referencedWarning.setKey("church.event.church.referenced");
            referencedWarning.addParam(churchWorshipService.getId());
            return referencedWarning;
        }
        final Hymn churchHymn = hymnRepository.findFirstByChurch(church);
        if (churchHymn != null) {
            referencedWarning.setKey("church.hymn.church.referenced");
            referencedWarning.addParam(churchHymn.getId());
            return referencedWarning;
        }
        final Expense churchExpense = expenseRepository.findFirstByChurch(church);
        if (churchExpense != null) {
            referencedWarning.setKey("church.transaction.church.referenced");
            referencedWarning.addParam(churchExpense.getId());
            return referencedWarning;
        }
        final Income churchIncome = incomeRepository.findFirstByChurch(church);
        if (churchIncome != null) {
            referencedWarning.setKey("church.transaction.church.referenced");
            referencedWarning.addParam(churchIncome.getId());
            return referencedWarning;
        }
        return null;
    }

}
