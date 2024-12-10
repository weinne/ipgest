package br.com.ipgest.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.ipgest.model.Igreja;
import br.com.ipgest.repository.IgrejaRepository;

@Service
public class IgrejaService {

    @Autowired
    private IgrejaRepository igrejaRepository;

    public List<Igreja> findAll() {
        return igrejaRepository.findAll();
    }

    public Igreja findById(Long id) {
        return igrejaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Igreja não encontrada!"));
    }

    public Igreja save(Igreja igreja) {
        return igrejaRepository.save(igreja);
    }

    public void deleteById(Long id) {
        igrejaRepository.deleteById(id);
    }
}
