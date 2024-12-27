package br.com.ipgest.ipgest.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class CounselingSessionDTO {

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

    private String tasks;

    private UUID church;

}
