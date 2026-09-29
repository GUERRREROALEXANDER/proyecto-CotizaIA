package com.cotizaia.workflow;
import com.cotizaia.domain.EstadoPropuesta;

public final class Rechazada implements EstadoRevision {
    public EstadoPropuesta nombre() { return EstadoPropuesta.RECHAZADA; }
}
