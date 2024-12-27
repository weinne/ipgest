package br.com.ipgest.ipgest.repos;

import br.com.ipgest.ipgest.domain.Church;
import br.com.ipgest.ipgest.domain.Hymn;
import org.springframework.data.jpa.repository.JpaRepository;


public interface HymnRepository extends JpaRepository<Hymn, Long> {

    Hymn findFirstByChurch(Church church);

}
