package br.com.ipgest.ipgest.service;

import br.com.ipgest.ipgest.domain.Appointment;
import br.com.ipgest.ipgest.domain.Church;
import br.com.ipgest.ipgest.model.AppointmentDTO;
import br.com.ipgest.ipgest.repos.AppointmentRepository;
import br.com.ipgest.ipgest.repos.ChurchRepository;
import br.com.ipgest.ipgest.util.NotFoundException;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final ChurchRepository churchRepository;

    public AppointmentService(final AppointmentRepository appointmentRepository,
            final ChurchRepository churchRepository) {
        this.appointmentRepository = appointmentRepository;
        this.churchRepository = churchRepository;
    }

    public List<AppointmentDTO> findAll() {
        final List<Appointment> appointments = appointmentRepository.findAll(Sort.by("id"));
        return appointments.stream()
                .map(appointment -> mapToDTO(appointment, new AppointmentDTO()))
                .toList();
    }

    public AppointmentDTO get(final UUID id) {
        return appointmentRepository.findById(id)
                .map(appointment -> mapToDTO(appointment, new AppointmentDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public UUID create(final AppointmentDTO appointmentDTO) {
        final Appointment appointment = new Appointment();
        mapToEntity(appointmentDTO, appointment);
        return appointmentRepository.save(appointment).getId();
    }

    public void update(final UUID id, final AppointmentDTO appointmentDTO) {
        final Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(appointmentDTO, appointment);
        appointmentRepository.save(appointment);
    }

    public void delete(final UUID id) {
        appointmentRepository.deleteById(id);
    }

    private AppointmentDTO mapToDTO(final Appointment appointment,
            final AppointmentDTO appointmentDTO) {
        appointmentDTO.setId(appointment.getId());
        appointmentDTO.setSubject(appointment.getSubject());
        appointmentDTO.setStartTime(appointment.getStartTime());
        appointmentDTO.setEndTime(appointment.getEndTime());
        appointmentDTO.setLocal(appointment.getLocal());
        appointmentDTO.setNotes(appointment.getNotes());
        appointmentDTO.setType(appointment.getType());
        appointmentDTO.setChurch(appointment.getChurch() == null ? null : appointment.getChurch().getId());
        return appointmentDTO;
    }

    private Appointment mapToEntity(final AppointmentDTO appointmentDTO,
            final Appointment appointment) {
        appointment.setSubject(appointmentDTO.getSubject());
        appointment.setStartTime(appointmentDTO.getStartTime());
        appointment.setEndTime(appointmentDTO.getEndTime());
        appointment.setLocal(appointmentDTO.getLocal());
        appointment.setNotes(appointmentDTO.getNotes());
        appointment.setType(appointmentDTO.getType());
        final Church church = appointmentDTO.getChurch() == null ? null : churchRepository.findById(appointmentDTO.getChurch())
                .orElseThrow(() -> new NotFoundException("church not found"));
        appointment.setChurch(church);
        return appointment;
    }

}
