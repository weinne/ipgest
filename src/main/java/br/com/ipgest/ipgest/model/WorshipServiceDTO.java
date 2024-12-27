package br.com.ipgest.ipgest.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class WorshipServiceDTO {

    private UUID id;

    @NotNull
    @Size(max = 255)
    private String subject;

    @NotNull
    private LocalDateTime startTime;

    @NotNull
    private LocalDateTime endTime;

    @Size(max = 255)
    private String local;

    private String notes;

    @Size(max = 255)
    private String preacher;

    @Size(max = 255)
    private String sermonText;

    private String liturgy;

    private UUID church;

    private List<Long> hymns;

}
