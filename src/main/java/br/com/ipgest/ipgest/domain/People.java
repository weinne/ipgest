package br.com.ipgest.ipgest.domain;

import br.com.ipgest.ipgest.model.Gender;
import br.com.ipgest.ipgest.model.MaritalStatus;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public abstract class People {

    @Id
    @Column(nullable = false, updatable = false, columnDefinition = "UUID")
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    @Column(nullable = false, length = 30)
    private String fullName;

    @Column(length = 20)
    private String phone;

    @Column(length = 20)
    private String email;

    @Column(length = 30)
    private String address;

    @Column(length = 20)
    private String city;

    @Column(length = 9)
    private String postalCode;

    @Column(length = 20)
    private String birthCity;

    @Column(length = 20)
    private String state;

    @Column(length = 11)
    private String registrationNumber;

    @Column(nullable = false, length = 14)
    private String cpf;

    @Column
    private LocalDate birthDate;

    @Column
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column
    @Enumerated(EnumType.STRING)
    private MaritalStatus maritalStatus;

    @Column
    private LocalDate dateOfDeath;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private OffsetDateTime dateCreated;

    @LastModifiedDate
    @Column(nullable = false)
    private OffsetDateTime lastUpdated;

}
