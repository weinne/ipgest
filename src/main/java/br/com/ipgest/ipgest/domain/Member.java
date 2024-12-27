package br.com.ipgest.ipgest.domain;

import br.com.ipgest.ipgest.model.AdmissionMode;
import br.com.ipgest.ipgest.model.ExitReason;
import br.com.ipgest.ipgest.model.MemberStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;

@Entity
@Getter
@Setter
@FilterDef(name = "tenantFilter", parameters = @ParamDef(name = "tenantId", type = UUID.class))
@Filter(name = "tenantFilter", condition = "church_id = :tenantId")
public class Member extends People {

    @Column
    private String previousReligion;

    @Column
    @Enumerated(EnumType.STRING)
    private MemberStatus status;

    @Column
    private LocalDate admissionDate;

    @Column
    @Enumerated(EnumType.STRING)
    private AdmissionMode admissionMode;

    @Column
    private LocalDate exitDate;

    @Column
    @Enumerated(EnumType.STRING)
    private ExitReason exitReason;

    @Column
    private LocalDate baptismDate;

    @Column
    private LocalDate profissionDate;

    @Column
    private LocalDate transferDate;

    @Column
    private String notes;

    @ManyToOne
    @JoinColumn(name = "church_id", nullable = false)
    private Church church;

    @Column(nullable = false)
    private Long tenantId;

    @ManyToMany
    @JoinTable(
            name = "MemberGroup",
            joinColumns = @JoinColumn(name = "memberId"),
            inverseJoinColumns = @JoinColumn(name = "groupId")
    )
    private Set<Group> groups;

    @ManyToMany(mappedBy = "members")
    private Set<Task> tasks;

}
