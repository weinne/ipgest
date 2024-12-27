package br.com.ipgest.ipgest.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class GroupDTO {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String name;

    private String description;

    private LocalDate establishedDate;

    @NotNull
    private GroupType type;

    private UUID church;

}
