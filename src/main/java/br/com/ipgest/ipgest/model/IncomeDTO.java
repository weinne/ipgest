package br.com.ipgest.ipgest.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class IncomeDTO {

    private UUID id;

    @NotNull
    @Size(max = 255)
    private String description;

    @NotNull
    @Digits(integer = 10, fraction = 2)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal amount;

    @NotNull
    private LocalDate date;

    @Size(max = 255)
    private String paymentMethod;

    @Size(max = 255)
    private String supplierOrClient;

    @Size(max = 255)
    private String invoice;

    @Size(max = 255)
    private String type;

    private UUID church;

}
