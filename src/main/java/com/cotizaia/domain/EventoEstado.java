package com.cotizaia.domain;
import java.time.Instant;
import java.util.UUID;

/** Revisor declarado en la petición, no identidad autenticada. */
public record EventoEstado(UUID propuestaId, EstadoPropuesta anterior,
        EstadoPropuesta nuevo, Instant fecha, String revisor, String motivo) { }
