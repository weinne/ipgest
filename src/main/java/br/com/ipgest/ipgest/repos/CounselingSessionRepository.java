package br.com.ipgest.ipgest.repos;

import br.com.ipgest.ipgest.domain.Church;
import br.com.ipgest.ipgest.domain.CounselingSession;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CounselingSessionRepository extends JpaRepository<CounselingSession, UUID> {

    CounselingSession findFirstByChurch(Church church);

}
