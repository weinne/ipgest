package br.com.ipgest.ipgest.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class SubscriptionDTO {

    private Long id;

    @NotNull
    private LocalDate startDate;

    private LocalDate endDate;

    @NotNull
    private SubscriptionStatus status;

    @NotNull
    private PaymentProvider paymentProvider;

    @NotNull
    private PaymentStatus paymentStatus;

    @NotNull
    @Size(max = 255)
    private String paymentReference;

    @NotNull
    private Long plan;

    @NotNull
    private UUID church;

}
