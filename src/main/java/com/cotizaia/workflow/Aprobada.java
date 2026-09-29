package com.cotizaia.workflow;
import com.cotizaia.domain.EstadoPropuesta;

public final class Aprobada implements EstadoRevision {
    public EstadoPropuesta nombre() { return EstadoPropuesta.APROBADA; }
}
