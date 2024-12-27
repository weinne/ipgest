package br.com.ipgest.ipgest.repos;

import br.com.ipgest.ipgest.domain.Plan;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PlanRepository extends JpaRepository<Plan, Long> {
}
