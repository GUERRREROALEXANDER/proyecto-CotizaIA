package com.cotizaia.api;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record CrearBriefRequest(
        @NotBlank @Size(max = 120) String cliente,
        @NotBlank @Size(min = 10, max = 10000) String texto,
        @NotNull @DecimalMin("0.01") @Digits(integer = 9, fraction = 2) BigDecimal tarifa) { }
