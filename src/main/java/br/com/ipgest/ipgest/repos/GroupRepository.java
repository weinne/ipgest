package br.com.ipgest.ipgest.repos;

import br.com.ipgest.ipgest.domain.Church;
import br.com.ipgest.ipgest.domain.Group;
import org.springframework.data.jpa.repository.JpaRepository;


public interface GroupRepository extends JpaRepository<Group, Long> {

    Group findFirstByChurch(Church church);

}
