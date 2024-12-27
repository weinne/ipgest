package br.com.ipgest.ipgest.config;

import org.hibernate.Filter;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

@Component
public class TenantEntityListener {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private TenantContext tenantContext;

    @PrePersist
    @PreUpdate
    public void setTenantFilter() {
        Session session = entityManager.unwrap(Session.class);
        Filter filter = session.enableFilter("tenantFilter");
        filter.setParameter("tenantId", tenantContext.getCurrentTenantId());
    }
}
