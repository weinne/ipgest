package br.com.ipgest.ipgest.service;

import br.com.ipgest.ipgest.domain.Church;
import br.com.ipgest.ipgest.domain.Plan;
import br.com.ipgest.ipgest.domain.Subscription;
import br.com.ipgest.ipgest.model.SubscriptionDTO;
import br.com.ipgest.ipgest.repos.ChurchRepository;
import br.com.ipgest.ipgest.repos.PlanRepository;
import br.com.ipgest.ipgest.repos.SubscriptionRepository;
import br.com.ipgest.ipgest.util.NotFoundException;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final PlanRepository planRepository;
    private final ChurchRepository churchRepository;

    public SubscriptionService(final SubscriptionRepository subscriptionRepository,
            final PlanRepository planRepository, final ChurchRepository churchRepository) {
        this.subscriptionRepository = subscriptionRepository;
        this.planRepository = planRepository;
        this.churchRepository = churchRepository;
    }

    public List<SubscriptionDTO> findAll() {
        final List<Subscription> subscriptions = subscriptionRepository.findAll(Sort.by("id"));
        return subscriptions.stream()
                .map(subscription -> mapToDTO(subscription, new SubscriptionDTO()))
                .toList();
    }

    public SubscriptionDTO get(final Long id) {
        return subscriptionRepository.findById(id)
                .map(subscription -> mapToDTO(subscription, new SubscriptionDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final SubscriptionDTO subscriptionDTO) {
        final Subscription subscription = new Subscription();
        mapToEntity(subscriptionDTO, subscription);
        return subscriptionRepository.save(subscription).getId();
    }

    public void update(final Long id, final SubscriptionDTO subscriptionDTO) {
        final Subscription subscription = subscriptionRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(subscriptionDTO, subscription);
        subscriptionRepository.save(subscription);
    }

    public void delete(final Long id) {
        subscriptionRepository.deleteById(id);
    }

    private SubscriptionDTO mapToDTO(final Subscription subscription,
            final SubscriptionDTO subscriptionDTO) {
        subscriptionDTO.setId(subscription.getId());
        subscriptionDTO.setStartDate(subscription.getStartDate());
        subscriptionDTO.setEndDate(subscription.getEndDate());
        subscriptionDTO.setStatus(subscription.getStatus());
        subscriptionDTO.setPaymentProvider(subscription.getPaymentProvider());
        subscriptionDTO.setPaymentStatus(subscription.getPaymentStatus());
        subscriptionDTO.setPaymentReference(subscription.getPaymentReference());
        subscriptionDTO.setPlan(subscription.getPlan() == null ? null : subscription.getPlan().getId());
        subscriptionDTO.setChurch(subscription.getChurch() == null ? null : subscription.getChurch().getId());
        return subscriptionDTO;
    }

    private Subscription mapToEntity(final SubscriptionDTO subscriptionDTO,
            final Subscription subscription) {
        subscription.setStartDate(subscriptionDTO.getStartDate());
        subscription.setEndDate(subscriptionDTO.getEndDate());
        subscription.setStatus(subscriptionDTO.getStatus());
        subscription.setPaymentProvider(subscriptionDTO.getPaymentProvider());
        subscription.setPaymentStatus(subscriptionDTO.getPaymentStatus());
        subscription.setPaymentReference(subscriptionDTO.getPaymentReference());
        final Plan plan = subscriptionDTO.getPlan() == null ? null : planRepository.findById(subscriptionDTO.getPlan())
                .orElseThrow(() -> new NotFoundException("plan not found"));
        subscription.setPlan(plan);
        final Church church = subscriptionDTO.getChurch() == null ? null : churchRepository.findById(subscriptionDTO.getChurch())
                .orElseThrow(() -> new NotFoundException("church not found"));
        subscription.setChurch(church);
        return subscription;
    }

}
