package br.com.ipgest.ipgest.repos;

import br.com.ipgest.ipgest.domain.Church;
import br.com.ipgest.ipgest.domain.Group;
import br.com.ipgest.ipgest.domain.Member;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;


public interface MemberRepository extends JpaRepository<Member, UUID> {

    Member findFirstByChurch(Church church);

    Member findFirstByGroups(Group group);

    List<Member> findAllByGroups(Group group);

    List<Member> findByChurchId(UUID churchId);

}
