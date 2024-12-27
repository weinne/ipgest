package br.com.ipgest.ipgest.service;

import br.com.ipgest.ipgest.domain.Church;
import br.com.ipgest.ipgest.domain.Hymn;
import br.com.ipgest.ipgest.domain.WorshipService;
import br.com.ipgest.ipgest.model.WorshipServiceDTO;
import br.com.ipgest.ipgest.repos.ChurchRepository;
import br.com.ipgest.ipgest.repos.HymnRepository;
import br.com.ipgest.ipgest.repos.WorshipServiceRepository;
import br.com.ipgest.ipgest.util.NotFoundException;
import jakarta.transaction.Transactional;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
@Transactional
public class WorshipServiceService {

    private final WorshipServiceRepository worshipServiceRepository;
    private final ChurchRepository churchRepository;
    private final HymnRepository hymnRepository;

    public WorshipServiceService(final WorshipServiceRepository worshipServiceRepository,
            final ChurchRepository churchRepository, final HymnRepository hymnRepository) {
        this.worshipServiceRepository = worshipServiceRepository;
        this.churchRepository = churchRepository;
        this.hymnRepository = hymnRepository;
    }

    public List<WorshipServiceDTO> findAll() {
        final List<WorshipService> worshipServices = worshipServiceRepository.findAll(Sort.by("id"));
        return worshipServices.stream()
                .map(worshipService -> mapToDTO(worshipService, new WorshipServiceDTO()))
                .toList();
    }

    public WorshipServiceDTO get(final UUID id) {
        return worshipServiceRepository.findById(id)
                .map(worshipService -> mapToDTO(worshipService, new WorshipServiceDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public UUID create(final WorshipServiceDTO worshipServiceDTO) {
        final WorshipService worshipService = new WorshipService();
        mapToEntity(worshipServiceDTO, worshipService);
        return worshipServiceRepository.save(worshipService).getId();
    }

    public void update(final UUID id, final WorshipServiceDTO worshipServiceDTO) {
        final WorshipService worshipService = worshipServiceRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(worshipServiceDTO, worshipService);
        worshipServiceRepository.save(worshipService);
    }

    public void delete(final UUID id) {
        worshipServiceRepository.deleteById(id);
    }

    private WorshipServiceDTO mapToDTO(final WorshipService worshipService,
            final WorshipServiceDTO worshipServiceDTO) {
        worshipServiceDTO.setId(worshipService.getId());
        worshipServiceDTO.setSubject(worshipService.getSubject());
        worshipServiceDTO.setStartTime(worshipService.getStartTime());
        worshipServiceDTO.setEndTime(worshipService.getEndTime());
        worshipServiceDTO.setLocal(worshipService.getLocal());
        worshipServiceDTO.setNotes(worshipService.getNotes());
        worshipServiceDTO.setPreacher(worshipService.getPreacher());
        worshipServiceDTO.setSermonText(worshipService.getSermonText());
        worshipServiceDTO.setLiturgy(worshipService.getLiturgy());
        worshipServiceDTO.setChurch(worshipService.getChurch() == null ? null : worshipService.getChurch().getId());
        worshipServiceDTO.setHymns(worshipService.getHymns().stream()
                .map(hymn -> hymn.getId())
                .toList());
        return worshipServiceDTO;
    }

    private WorshipService mapToEntity(final WorshipServiceDTO worshipServiceDTO,
            final WorshipService worshipService) {
        worshipService.setSubject(worshipServiceDTO.getSubject());
        worshipService.setStartTime(worshipServiceDTO.getStartTime());
        worshipService.setEndTime(worshipServiceDTO.getEndTime());
        worshipService.setLocal(worshipServiceDTO.getLocal());
        worshipService.setNotes(worshipServiceDTO.getNotes());
        worshipService.setPreacher(worshipServiceDTO.getPreacher());
        worshipService.setSermonText(worshipServiceDTO.getSermonText());
        worshipService.setLiturgy(worshipServiceDTO.getLiturgy());
        final Church church = worshipServiceDTO.getChurch() == null ? null : churchRepository.findById(worshipServiceDTO.getChurch())
                .orElseThrow(() -> new NotFoundException("church not found"));
        worshipService.setChurch(church);
        final List<Hymn> hymns = hymnRepository.findAllById(
                worshipServiceDTO.getHymns() == null ? Collections.emptyList() : worshipServiceDTO.getHymns());
        if (hymns.size() != (worshipServiceDTO.getHymns() == null ? 0 : worshipServiceDTO.getHymns().size())) {
            throw new NotFoundException("one of hymns not found");
        }
        worshipService.setHymns(new HashSet<>(hymns));
        return worshipService;
    }

}
