package com.cotizaia.api;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record DecisionRequest(
        @NotBlank @Size(max = 120) String revisor,
        @NotBlank @Size(max = 1000) String motivo) { }
