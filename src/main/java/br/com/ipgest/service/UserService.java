package br.com.ipgest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.ipgest.model.User;
import br.com.ipgest.repository.UserRepository;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        System.out.println("Usuário encontrado no banco" + user);
        return user;
    }

    public User getLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
            new SecurityContextLogoutHandler().logout(null, null, authentication);
            return null;
        }
        
        User user = (User) authentication.getPrincipal();

        System.out.println("Usuário logado: " + user);

        user = getUserById(user.getId()); // Busca o usuário no banco de dados
        user.getIgrejas().size(); // Inicializa a coleção `igrejas`

        return user;
    }

    @Transactional
    public User getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + id));
        user.getIgrejas().size(); // Inicializa a coleção `igrejas`
        return user;
    }

    public User save(User user) {
        return userRepository.save(user);
    }
}
