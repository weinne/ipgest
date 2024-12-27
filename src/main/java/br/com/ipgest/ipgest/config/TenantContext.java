package br.com.ipgest.ipgest.config;

import java.util.UUID;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import br.com.ipgest.ipgest.domain.User;

@Component
public class TenantContext {

    public UUID getCurrentTenantId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new IllegalStateException("No authentication found in security context");
        }

        User user = (User) authentication.getPrincipal();
        if (user.getChurch() != null) {
            return user.getChurch().getId();
        } else {
            throw new IllegalStateException("User does not belong to any church");
        }
    }
}
