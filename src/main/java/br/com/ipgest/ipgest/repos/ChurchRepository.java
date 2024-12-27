package br.com.ipgest.ipgest.repos;

import br.com.ipgest.ipgest.domain.Church;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ChurchRepository extends JpaRepository<Church, UUID> {
}
