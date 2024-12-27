package br.com.ipgest.ipgest.repos;

import br.com.ipgest.ipgest.domain.Church;
import br.com.ipgest.ipgest.domain.Income;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;


public interface IncomeRepository extends JpaRepository<Income, UUID> {

    Income findFirstByChurch(Church church);

}
