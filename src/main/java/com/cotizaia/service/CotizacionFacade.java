package com.cotizaia.service;
import com.cotizaia.agent.ContextoBrief;
import com.cotizaia.agent.HandlerBrief;
import com.cotizaia.domain.Brief;
import com.cotizaia.domain.Propuesta;
import com.cotizaia.pricing.EstrategiaPrecio;
import com.cotizaia.repository.RepositorioPropuestas;
import com.cotizaia.workflow.PublicadorEventos;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

/** Fachada del caso de uso: los controladores no coordinan patrones ni reglas. */
@Service
public class CotizacionFacade {
    private final HandlerBrief cadena;
    private final EstrategiaPrecio precio;
    private final RepositorioPropuestas repositorio;
    private final PublicadorEventos eventos;
    public CotizacionFacade(HandlerBrief cadena, EstrategiaPrecio precio,
            RepositorioPropuestas repositorio, PublicadorEventos eventos) {
        this.cadena = cadena; this.precio = precio;
        this.repositorio = repositorio; this.eventos = eventos;
    }
    public Propuesta procesar(Brief brief) {
        ContextoBrief contexto = new ContextoBrief(brief);
        cadena.procesar(contexto);
        Propuesta propuesta = Propuesta.builder().brief(brief)
                .requisitos(contexto.requisitos()).preguntas(contexto.preguntas())
                .desglose(contexto.desglose())
                .precioTotal(precio.calcular(contexto.desglose(), brief.tarifa())).build();
        repositorio.guardar(propuesta);
        return propuesta;
    }
    public List<Propuesta> listar() { return repositorio.listar(); }
    public Propuesta consultar(UUID id) {
        return repositorio.buscar(id).orElseThrow(() -> new PropuestaNoEncontrada(id));
    }
    public Propuesta decidir(UUID id, boolean aprobar, String revisor, String motivo) {
        Propuesta propuesta = consultar(id);
        synchronized (propuesta) {
            eventos.publicar(propuesta.decidir(aprobar, revisor, motivo));
            return propuesta;
        }
    }
}
