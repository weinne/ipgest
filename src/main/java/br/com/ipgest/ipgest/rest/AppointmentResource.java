package br.com.ipgest.ipgest.rest;

import br.com.ipgest.ipgest.model.AppointmentDTO;
import br.com.ipgest.ipgest.service.AppointmentService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/api/appointments", produces = MediaType.APPLICATION_JSON_VALUE)
public class AppointmentResource {

    private final AppointmentService appointmentService;

    public AppointmentResource(final AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @GetMapping
    public ResponseEntity<List<AppointmentDTO>> getAllAppointments() {
        return ResponseEntity.ok(appointmentService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppointmentDTO> getAppointment(@PathVariable(name = "id") final UUID id) {
        return ResponseEntity.ok(appointmentService.get(id));
    }

    @PostMapping
    public ResponseEntity<UUID> createAppointment(
            @RequestBody @Valid final AppointmentDTO appointmentDTO) {
        final UUID createdId = appointmentService.create(appointmentDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UUID> updateAppointment(@PathVariable(name = "id") final UUID id,
            @RequestBody @Valid final AppointmentDTO appointmentDTO) {
        appointmentService.update(id, appointmentDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAppointment(@PathVariable(name = "id") final UUID id) {
        appointmentService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
