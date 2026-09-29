package com.cotizaia.workflow;
import com.cotizaia.domain.EventoEstado;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.springframework.stereotype.Component;

@Component
public class HistorialEventos implements ObservadorEstado {
    private final ConcurrentLinkedQueue<EventoEstado> eventos = new ConcurrentLinkedQueue<>();
    public void alCambiar(EventoEstado evento) { eventos.add(evento); }
    public List<EventoEstado> dePropuesta(UUID id) {
        return eventos.stream().filter(e -> e.propuestaId().equals(id)).toList();
    }
}
