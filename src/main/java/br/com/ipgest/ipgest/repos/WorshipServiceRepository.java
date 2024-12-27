package br.com.ipgest.ipgest.repos;

import br.com.ipgest.ipgest.domain.Church;
import br.com.ipgest.ipgest.domain.Hymn;
import br.com.ipgest.ipgest.domain.WorshipService;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;


public interface WorshipServiceRepository extends JpaRepository<WorshipService, UUID> {

    WorshipService findFirstByChurch(Church church);

    WorshipService findFirstByHymns(Hymn hymn);

    List<WorshipService> findAllByHymns(Hymn hymn);

}
