package br.com.ipgest.ipgest.repos;

import br.com.ipgest.ipgest.domain.Church;
import br.com.ipgest.ipgest.domain.Plan;
import br.com.ipgest.ipgest.domain.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;


public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    Subscription findFirstByPlan(Plan plan);

    Subscription findFirstByChurch(Church church);

}
