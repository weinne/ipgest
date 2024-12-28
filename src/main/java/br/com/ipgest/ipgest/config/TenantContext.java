package br.com.ipgest.ipgest.config;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import br.com.ipgest.ipgest.domain.User;
import br.com.ipgest.ipgest.repos.UserRepository;

@Component
public class TenantContext {

    @Autowired
    private UserRepository userRepository;

    public UUID getCurrentTenantId() {

        
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal == null) {
            throw new IllegalStateException("No authentication found in security context");
        }

        if (principal instanceof UserDetails userDetails) {
            String username = userDetails.getUsername();
            Optional<User> user = userRepository.findByUsername(username);
            return user.get().getChurch().getId();
        } else {
            throw new IllegalStateException("User does not belong to any church");
        }
    }
}
