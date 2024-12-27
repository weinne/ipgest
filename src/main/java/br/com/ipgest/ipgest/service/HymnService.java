package br.com.ipgest.ipgest.service;

import br.com.ipgest.ipgest.domain.Church;
import br.com.ipgest.ipgest.domain.Hymn;
import br.com.ipgest.ipgest.model.HymnDTO;
import br.com.ipgest.ipgest.repos.ChurchRepository;
import br.com.ipgest.ipgest.repos.HymnRepository;
import br.com.ipgest.ipgest.repos.WorshipServiceRepository;
import br.com.ipgest.ipgest.util.NotFoundException;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
@Transactional
public class HymnService {

    private final HymnRepository hymnRepository;
    private final ChurchRepository churchRepository;
    private final WorshipServiceRepository worshipServiceRepository;

    public HymnService(final HymnRepository hymnRepository, final ChurchRepository churchRepository,
            final WorshipServiceRepository worshipServiceRepository) {
        this.hymnRepository = hymnRepository;
        this.churchRepository = churchRepository;
        this.worshipServiceRepository = worshipServiceRepository;
    }

    public List<HymnDTO> findAll() {
        final List<Hymn> hymns = hymnRepository.findAll(Sort.by("id"));
        return hymns.stream()
                .map(hymn -> mapToDTO(hymn, new HymnDTO()))
                .toList();
    }

    public HymnDTO get(final Long id) {
        return hymnRepository.findById(id)
                .map(hymn -> mapToDTO(hymn, new HymnDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final HymnDTO hymnDTO) {
        final Hymn hymn = new Hymn();
        mapToEntity(hymnDTO, hymn);
        return hymnRepository.save(hymn).getId();
    }

    public void update(final Long id, final HymnDTO hymnDTO) {
        final Hymn hymn = hymnRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(hymnDTO, hymn);
        hymnRepository.save(hymn);
    }

    public void delete(final Long id) {
        final Hymn hymn = hymnRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        // remove many-to-many relations at owning side
        worshipServiceRepository.findAllByHymns(hymn)
                .forEach(worshipService -> worshipService.getHymns().remove(hymn));
        hymnRepository.delete(hymn);
    }

    private HymnDTO mapToDTO(final Hymn hymn, final HymnDTO hymnDTO) {
        hymnDTO.setId(hymn.getId());
        hymnDTO.setTitle(hymn.getTitle());
        hymnDTO.setLyricsAuthor(hymn.getLyricsAuthor());
        hymnDTO.setMusicAuthor(hymn.getMusicAuthor());
        hymnDTO.setHymnary(hymn.getHymnary());
        hymnDTO.setHymnNumber(hymn.getHymnNumber());
        hymnDTO.setLink(hymn.getLink());
        hymnDTO.setTone(hymn.getTone());
        hymnDTO.setLyrics(hymn.getLyrics());
        hymnDTO.setChurch(hymn.getChurch() == null ? null : hymn.getChurch().getId());
        return hymnDTO;
    }

    private Hymn mapToEntity(final HymnDTO hymnDTO, final Hymn hymn) {
        hymn.setTitle(hymnDTO.getTitle());
        hymn.setLyricsAuthor(hymnDTO.getLyricsAuthor());
        hymn.setMusicAuthor(hymnDTO.getMusicAuthor());
        hymn.setHymnary(hymnDTO.getHymnary());
        hymn.setHymnNumber(hymnDTO.getHymnNumber());
        hymn.setLink(hymnDTO.getLink());
        hymn.setTone(hymnDTO.getTone());
        hymn.setLyrics(hymnDTO.getLyrics());
        final Church church = hymnDTO.getChurch() == null ? null : churchRepository.findById(hymnDTO.getChurch())
                .orElseThrow(() -> new NotFoundException("church not found"));
        hymn.setChurch(church);
        return hymn;
    }

}
