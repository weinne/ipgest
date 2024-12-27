package br.com.ipgest.ipgest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.ipgest.ipgest.domain.User;
import br.com.ipgest.ipgest.repos.UserRepository;

@Service
public class AuthService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return user;
    }

    @Transactional
    public User getLoggedInUser() throws UsernameNotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            User user = (User) authentication.getPrincipal();
            try {
                user = (User) loadUserByUsername(user.getUsername());
            } catch (UsernameNotFoundException e) {
                throw new UsernameNotFoundException("Usuário não encontrado.");
            }
            return user;
        }
        throw new UsernameNotFoundException("Usuário não autenticado.");
    }

    @Transactional
    public User getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + id));
        return user;
    }

    public User save(User user) {
        return userRepository.save(user);
    }
}
