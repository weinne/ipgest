package br.com.ipgest.ipgest.service;

import br.com.ipgest.ipgest.domain.Plan;
import br.com.ipgest.ipgest.domain.Subscription;
import br.com.ipgest.ipgest.model.PlanDTO;
import br.com.ipgest.ipgest.repos.PlanRepository;
import br.com.ipgest.ipgest.repos.SubscriptionRepository;
import br.com.ipgest.ipgest.util.NotFoundException;
import br.com.ipgest.ipgest.util.ReferencedWarning;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class PlanService {

    private final PlanRepository planRepository;
    private final SubscriptionRepository subscriptionRepository;

    public PlanService(final PlanRepository planRepository,
            final SubscriptionRepository subscriptionRepository) {
        this.planRepository = planRepository;
        this.subscriptionRepository = subscriptionRepository;
    }

    public List<PlanDTO> findAll() {
        final List<Plan> plans = planRepository.findAll(Sort.by("id"));
        return plans.stream()
                .map(plan -> mapToDTO(plan, new PlanDTO()))
                .toList();
    }

    public PlanDTO get(final Long id) {
        return planRepository.findById(id)
                .map(plan -> mapToDTO(plan, new PlanDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final PlanDTO planDTO) {
        final Plan plan = new Plan();
        mapToEntity(planDTO, plan);
        return planRepository.save(plan).getId();
    }

    public void update(final Long id, final PlanDTO planDTO) {
        final Plan plan = planRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(planDTO, plan);
        planRepository.save(plan);
    }

    public void delete(final Long id) {
        planRepository.deleteById(id);
    }

    private PlanDTO mapToDTO(final Plan plan, final PlanDTO planDTO) {
        planDTO.setId(plan.getId());
        planDTO.setName(plan.getName());
        planDTO.setPrice(plan.getPrice());
        planDTO.setDescription(plan.getDescription());
        planDTO.setFeatures(plan.getFeatures());
        planDTO.setRenewalPeriod(plan.getRenewalPeriod());
        return planDTO;
    }

    private Plan mapToEntity(final PlanDTO planDTO, final Plan plan) {
        plan.setName(planDTO.getName());
        plan.setPrice(planDTO.getPrice());
        plan.setDescription(planDTO.getDescription());
        plan.setFeatures(planDTO.getFeatures());
        plan.setRenewalPeriod(planDTO.getRenewalPeriod());
        return plan;
    }

    public ReferencedWarning getReferencedWarning(final Long id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Plan plan = planRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Subscription planSubscription = subscriptionRepository.findFirstByPlan(plan);
        if (planSubscription != null) {
            referencedWarning.setKey("plan.subscription.plan.referenced");
            referencedWarning.addParam(planSubscription.getId());
            return referencedWarning;
        }
        return null;
    }

}
