package com.cotizaia.workflow;
import com.cotizaia.domain.EstadoPropuesta;

public final class EnRevision implements EstadoRevision {
    public EstadoPropuesta nombre() { return EstadoPropuesta.EN_REVISION; }
    public EstadoRevision aprobar() { return new Aprobada(); }
    public EstadoRevision rechazar() { return new Rechazada(); }
}
