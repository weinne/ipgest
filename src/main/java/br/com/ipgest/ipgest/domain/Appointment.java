package br.com.ipgest.ipgest.domain;

import br.com.ipgest.ipgest.model.AppointmentType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
public class Appointment extends Event {

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AppointmentType type;

}
