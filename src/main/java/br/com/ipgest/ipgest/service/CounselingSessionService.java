package br.com.ipgest.ipgest.service;

import br.com.ipgest.ipgest.domain.Church;
import br.com.ipgest.ipgest.domain.CounselingSession;
import br.com.ipgest.ipgest.model.CounselingSessionDTO;
import br.com.ipgest.ipgest.repos.ChurchRepository;
import br.com.ipgest.ipgest.repos.CounselingSessionRepository;
import br.com.ipgest.ipgest.util.NotFoundException;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class CounselingSessionService {

    private final CounselingSessionRepository counselingSessionRepository;
    private final ChurchRepository churchRepository;

    public CounselingSessionService(final CounselingSessionRepository counselingSessionRepository,
            final ChurchRepository churchRepository) {
        this.counselingSessionRepository = counselingSessionRepository;
        this.churchRepository = churchRepository;
    }

    public List<CounselingSessionDTO> findAll() {
        final List<CounselingSession> counselingSessions = counselingSessionRepository.findAll(Sort.by("id"));
        return counselingSessions.stream()
                .map(counselingSession -> mapToDTO(counselingSession, new CounselingSessionDTO()))
                .toList();
    }

    public CounselingSessionDTO get(final UUID id) {
        return counselingSessionRepository.findById(id)
                .map(counselingSession -> mapToDTO(counselingSession, new CounselingSessionDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public UUID create(final CounselingSessionDTO counselingSessionDTO) {
        final CounselingSession counselingSession = new CounselingSession();
        mapToEntity(counselingSessionDTO, counselingSession);
        return counselingSessionRepository.save(counselingSession).getId();
    }

    public void update(final UUID id, final CounselingSessionDTO counselingSessionDTO) {
        final CounselingSession counselingSession = counselingSessionRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(counselingSessionDTO, counselingSession);
        counselingSessionRepository.save(counselingSession);
    }

    public void delete(final UUID id) {
        counselingSessionRepository.deleteById(id);
    }

    private CounselingSessionDTO mapToDTO(final CounselingSession counselingSession,
            final CounselingSessionDTO counselingSessionDTO) {
        counselingSessionDTO.setId(counselingSession.getId());
        counselingSessionDTO.setSubject(counselingSession.getSubject());
        counselingSessionDTO.setStartTime(counselingSession.getStartTime());
        counselingSessionDTO.setEndTime(counselingSession.getEndTime());
        counselingSessionDTO.setLocal(counselingSession.getLocal());
        counselingSessionDTO.setNotes(counselingSession.getNotes());
        counselingSessionDTO.setTasks(counselingSession.getTasks());
        counselingSessionDTO.setChurch(counselingSession.getChurch() == null ? null : counselingSession.getChurch().getId());
        return counselingSessionDTO;
    }

    private CounselingSession mapToEntity(final CounselingSessionDTO counselingSessionDTO,
            final CounselingSession counselingSession) {
        counselingSession.setSubject(counselingSessionDTO.getSubject());
        counselingSession.setStartTime(counselingSessionDTO.getStartTime());
        counselingSession.setEndTime(counselingSessionDTO.getEndTime());
        counselingSession.setLocal(counselingSessionDTO.getLocal());
        counselingSession.setNotes(counselingSessionDTO.getNotes());
        counselingSession.setTasks(counselingSessionDTO.getTasks());
        final Church church = counselingSessionDTO.getChurch() == null ? null : churchRepository.findById(counselingSessionDTO.getChurch())
                .orElseThrow(() -> new NotFoundException("church not found"));
        counselingSession.setChurch(church);
        return counselingSession;
    }

}
