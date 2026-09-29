package com.cotizaia.domain;

import com.cotizaia.workflow.EnRevision;
import com.cotizaia.workflow.EstadoRevision;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/** Datos de cotización inmutables y transición controlada por State. */
public final class Propuesta {
    private final UUID id;
    private final Brief brief;
    private final List<String> requisitos;
    private final List<String> preguntas;
    private final List<ItemEstimado> desglose;
    private final BigDecimal precioTotal;
    private final Instant fecha;
    private EstadoRevision estado = new EnRevision();

    private Propuesta(Builder b) {
        id = UUID.randomUUID();
        brief = Objects.requireNonNull(b.brief, "Falta brief");
        requisitos = List.copyOf(b.requisitos);
        preguntas = List.copyOf(b.preguntas);
        desglose = List.copyOf(b.desglose);
        precioTotal = Objects.requireNonNull(b.precioTotal, "Falta precio");
        fecha = Instant.now();
        if (desglose.isEmpty() || precioTotal.signum() <= 0) {
            throw new IllegalArgumentException("Se requieren desglose y precio positivo");
        }
    }
    public synchronized EventoEstado decidir(boolean aprobar, String revisor, String motivo) {
        if (revisor == null || revisor.isBlank() || motivo == null || motivo.isBlank()) {
            throw new IllegalArgumentException("La decisión requiere revisor y motivo");
        }
        EstadoPropuesta anterior = estado.nombre();
        estado = aprobar ? estado.aprobar() : estado.rechazar();
        return new EventoEstado(id, anterior, estado.nombre(), Instant.now(), revisor.strip(), motivo.strip());
    }
    public UUID getId() { return id; }
    public String getCliente() { return brief.cliente(); }
    public String getTextoOriginal() { return brief.texto(); }
    public List<String> getRequisitos() { return requisitos; }
    public List<String> getPreguntasAclaratorias() { return preguntas; }
    public List<ItemEstimado> getDesgloseHoras() { return desglose; }
    public int getHorasTotales() { return desglose.stream().mapToInt(ItemEstimado::horas).sum(); }
    public BigDecimal getTarifa() { return brief.tarifa(); }
    public BigDecimal getPrecioTotal() { return precioTotal; }
    public Instant getFecha() { return fecha; }
    public synchronized EstadoPropuesta getEstado() { return estado.nombre(); }
    public static Builder builder() { return new Builder(); }

    public static final class Builder {
        private Brief brief;
        private List<String> requisitos = List.of();
        private List<String> preguntas = List.of();
        private List<ItemEstimado> desglose = List.of();
        private BigDecimal precioTotal;
        private Builder() { }
        public Builder brief(Brief v) { brief = v; return this; }
        public Builder requisitos(List<String> v) { requisitos = List.copyOf(v); return this; }
        public Builder preguntas(List<String> v) { preguntas = List.copyOf(v); return this; }
        public Builder desglose(List<ItemEstimado> v) { desglose = List.copyOf(v); return this; }
        public Builder precioTotal(BigDecimal v) { precioTotal = v; return this; }
        public Propuesta build() { return new Propuesta(this); }
    }
}
