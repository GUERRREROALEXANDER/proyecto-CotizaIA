package com.cotizaia.workflow;
import com.cotizaia.domain.EventoEstado;
import java.util.List;
import org.springframework.stereotype.Component;

/** Observer síncrono: Spring registra todas las implementaciones disponibles. */
@Component
public class PublicadorEventos {
    private final List<ObservadorEstado> observadores;
    public PublicadorEventos(List<ObservadorEstado> observadores) {
        this.observadores = List.copyOf(observadores);
    }
    public void publicar(EventoEstado evento) {
        observadores.forEach(observador -> observador.alCambiar(evento));
    }
}
