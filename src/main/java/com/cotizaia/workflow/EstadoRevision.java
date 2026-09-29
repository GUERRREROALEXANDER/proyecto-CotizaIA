package com.cotizaia.workflow;
import com.cotizaia.domain.EstadoPropuesta;

/** Cada objeto de estado decide qué acciones permite. */
public interface EstadoRevision {
    EstadoPropuesta nombre();
    default EstadoRevision aprobar() {
        throw new TransicionInvalida("No se puede aprobar una propuesta " + nombre());
    }
    default EstadoRevision rechazar() {
        throw new TransicionInvalida("No se puede rechazar una propuesta " + nombre());
    }
}
