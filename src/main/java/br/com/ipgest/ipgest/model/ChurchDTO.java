package br.com.ipgest.ipgest.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ChurchDTO {

    private UUID id;

    @NotNull
    @Size(max = 19)
    private String cnpj;

    @NotNull
    @Size(max = 255)
    private String name;

    @Size(max = 10)
    private String abbreviation;

    @Size(max = 255)
    private String presbitery;

    @Size(max = 255)
    private String city;

    @Size(max = 255)
    private String state;

    @Size(max = 255)
    private String address;

    @NotNull
    private ChurchStatus status;

    private String about;

}
