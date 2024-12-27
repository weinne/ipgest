package br.com.ipgest.ipgest.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class TaskDTO {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String title;

    private String description;

    @NotNull
    private LocalDate dueDate;

    @NotNull
    private TaskStatus status;

    private TaskPriority priority;

    private String notes;

    private Long user;

    private List<UUID> members;

    private UUID church;

}
