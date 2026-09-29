package com.cotizaia.workflow;
import com.cotizaia.domain.EventoEstado;
public interface ObservadorEstado {
    void alCambiar(EventoEstado evento);
}
