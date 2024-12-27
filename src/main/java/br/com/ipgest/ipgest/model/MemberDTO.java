package br.com.ipgest.ipgest.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class MemberDTO {

    private UUID id;

    @NotNull
    @Size(max = 30)
    private String fullName;

    @Size(max = 20)
    private String phone;

    @Size(max = 20)
    private String email;

    @Size(max = 30)
    private String address;

    @Size(max = 20)
    private String city;

    @Size(max = 9)
    private String postalCode;

    @Size(max = 20)
    private String birthCity;

    @Size(max = 20)
    private String state;

    @Size(max = 11)
    private String registrationNumber;

    @NotNull
    @Size(max = 14)
    private String cpf;

    private LocalDate birthDate;

    private Gender gender;

    private MaritalStatus maritalStatus;

    private LocalDate dateOfDeath;

    @Size(max = 255)
    private String previousReligion;

    private MemberStatus status;

    private LocalDate admissionDate;

    private AdmissionMode admissionMode;

    private LocalDate exitDate;

    private ExitReason exitReason;

    private LocalDate baptismDate;

    private LocalDate profissionDate;

    private LocalDate transferDate;

    private String notes;

    private UUID church;

    private List<Long> groups;

}
