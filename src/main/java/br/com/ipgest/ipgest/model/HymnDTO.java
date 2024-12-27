package br.com.ipgest.ipgest.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class HymnDTO {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String title;

    @Size(max = 255)
    private String lyricsAuthor;

    @Size(max = 255)
    private String musicAuthor;

    @Size(max = 255)
    private String hymnary;

    private Long hymnNumber;

    @Size(max = 255)
    private String link;

    @Size(max = 5)
    private String tone;

    private String lyrics;

    private UUID church;

}
