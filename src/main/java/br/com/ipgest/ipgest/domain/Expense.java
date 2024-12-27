package br.com.ipgest.ipgest.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Filter;

@Entity
@Filter(name = "tenantFilter", condition = "church_id = :tenantId")
@Getter
@Setter
public class Expense extends Transaction {

    @Column
    private String category;

}
