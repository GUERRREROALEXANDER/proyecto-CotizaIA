package com.cotizaia.api;
import com.cotizaia.domain.Brief;
import com.cotizaia.domain.Propuesta;
import com.cotizaia.service.CotizacionFacade;
import com.cotizaia.workflow.HistorialEventos;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class CotizacionController {
    private final CotizacionFacade facade;
    private final HistorialEventos historial;
    public CotizacionController(CotizacionFacade facade, HistorialEventos historial) {
        this.facade = facade; this.historial = historial;
    }
    @PostMapping("/briefs")
    public ResponseEntity<PropuestaResponse> crear(@Valid @RequestBody CrearBriefRequest r) {
        Propuesta p = facade.procesar(new Brief(r.cliente().strip(), r.texto(), r.tarifa()));
        return ResponseEntity.created(URI.create("/api/propuestas/" + p.getId())).body(respuesta(p));
    }
    @GetMapping("/propuestas")
    public List<PropuestaResponse> listar() { return facade.listar().stream().map(this::respuesta).toList(); }
    @GetMapping("/propuestas/{id}")
    public PropuestaResponse consultar(@PathVariable UUID id) { return respuesta(facade.consultar(id)); }
    @PostMapping("/propuestas/{id}/aprobar")
    public PropuestaResponse aprobar(@PathVariable UUID id, @Valid @RequestBody DecisionRequest r) {
        return respuesta(facade.decidir(id, true, r.revisor(), r.motivo()));
    }
    @PostMapping("/propuestas/{id}/rechazar")
    public PropuestaResponse rechazar(@PathVariable UUID id, @Valid @RequestBody DecisionRequest r) {
        return respuesta(facade.decidir(id, false, r.revisor(), r.motivo()));
    }
    private PropuestaResponse respuesta(Propuesta p) {
        synchronized (p) {
            return PropuestaResponse.de(p, historial.dePropuesta(p.getId()));
        }
    }
}
