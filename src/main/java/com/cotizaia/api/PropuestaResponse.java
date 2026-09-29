package com.cotizaia.api;
import com.cotizaia.domain.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

/** Copia estable para HTTP; no expone el objeto mutable de State. */
public record PropuestaResponse(UUID id, String cliente, String textoOriginal,
        List<String> requisitos, List<String> preguntasAclaratorias,
        List<ItemEstimado> desgloseHoras, int horasTotales, BigDecimal tarifa,
        String moneda, BigDecimal precioTotal, Instant fecha, EstadoPropuesta estado,
        List<EventoEstado> historial) {
    public static PropuestaResponse de(Propuesta p, List<EventoEstado> historial) {
        return new PropuestaResponse(p.getId(), p.getCliente(), p.getTextoOriginal(),
                p.getRequisitos(), p.getPreguntasAclaratorias(), p.getDesgloseHoras(),
                p.getHorasTotales(), p.getTarifa(), "COP", p.getPrecioTotal(),
                p.getFecha(), p.getEstado(), List.copyOf(historial));
    }
}
