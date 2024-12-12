package br.com.ipgest.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.ipgest.model.Igreja;
import br.com.ipgest.model.User;

public interface IgrejaRepository extends JpaRepository<Igreja, Long> {
    List<Igreja> findByUsers(User user);
}
