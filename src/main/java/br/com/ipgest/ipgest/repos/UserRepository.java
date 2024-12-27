package br.com.ipgest.ipgest.repos;

import br.com.ipgest.ipgest.domain.Church;
import br.com.ipgest.ipgest.domain.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, Long> {

    User findFirstByChurch(Church church);

    Optional<User> findByUsername(String username);

    List<User> findByChurchId(UUID churchId);

}
