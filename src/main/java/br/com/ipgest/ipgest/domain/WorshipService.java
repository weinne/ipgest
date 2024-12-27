package br.com.ipgest.ipgest.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Filter;

@Entity
@Filter(name = "tenantFilter", condition = "church_id = :tenantId")
@Getter
@Setter
public class WorshipService extends Event {

    @Column
    private String preacher;

    @Column
    private String sermonText;

    @Column
    private String liturgy;

    @ManyToMany
    @JoinTable(
            name = "Hymnservice",
            joinColumns = @JoinColumn(name = "worshipServiceId"),
            inverseJoinColumns = @JoinColumn(name = "hymnId")
    )
    private Set<Hymn> hymns;

}
