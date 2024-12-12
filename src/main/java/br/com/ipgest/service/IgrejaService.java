package br.com.ipgest.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.ipgest.model.Igreja;
import br.com.ipgest.model.User;
import br.com.ipgest.repository.IgrejaRepository;
import br.com.ipgest.repository.UserRepository;

@Service
public class IgrejaService extends BaseService<Igreja, Long>  {

    @Autowired
    private IgrejaRepository igrejaRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Igreja> findByUser(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        return igrejaRepository.findByUsers(user);
    }
}
