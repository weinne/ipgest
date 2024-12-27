package br.com.ipgest.ipgest.repos;

import br.com.ipgest.ipgest.domain.Church;
import br.com.ipgest.ipgest.domain.Member;
import br.com.ipgest.ipgest.domain.Task;
import br.com.ipgest.ipgest.domain.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;


public interface TaskRepository extends JpaRepository<Task, Long> {

    Task findFirstByUser(User user);

    Task findFirstByMembers(Member member);

    Task findFirstByChurch(Church church);

    List<Task> findAllByMembers(Member member);

}
