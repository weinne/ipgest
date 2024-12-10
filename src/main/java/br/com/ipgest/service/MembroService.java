package br.com.ipgest.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.ipgest.model.Membro;
import br.com.ipgest.repository.MembroRepository;

@Service
public class MembroService {

    @Autowired
    private MembroRepository membroRepository;

    public List<Membro> findAll() {
        return membroRepository.findAll();
    }

    public Membro findById(Long id) {
        return membroRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Membro não encontrado!"));
    }

    public Membro save(Membro membro) {
        return membroRepository.save(membro);
    }

    public void deleteById(Long id) {
        membroRepository.deleteById(id);
    }
}
