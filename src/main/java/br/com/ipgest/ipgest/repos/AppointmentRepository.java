package br.com.ipgest.ipgest.repos;

import br.com.ipgest.ipgest.domain.Appointment;
import br.com.ipgest.ipgest.domain.Church;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AppointmentRepository extends JpaRepository<Appointment, UUID> {

    Appointment findFirstByChurch(Church church);

}
