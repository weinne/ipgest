package br.com.ipgest.ipgest.domain;

import br.com.ipgest.ipgest.model.ChurchStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
public class Church extends Organization {

    @Column(nullable = false)
    private String name;

    @Column(length = 10)
    private String abbreviation;

    @Column
    private String presbitery;

    @Column
    private String city;

    @Column
    private String state;

    @Column
    private String address;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ChurchStatus status;

    @Column
    private String about;

    @OneToMany(mappedBy = "church")
    private Set<User> users;

    @OneToMany(mappedBy = "church")
    private Set<Member> members;

    @OneToMany(mappedBy = "church")
    private Set<Subscription> subscriptions;

    @OneToMany(mappedBy = "church")
    private Set<Group> group;

    @OneToMany(mappedBy = "church")
    private Set<Task> tasks;

}
