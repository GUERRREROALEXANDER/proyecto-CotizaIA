package com.cotizaia;

import com.cotizaia.agent.*;
import com.cotizaia.domain.*;
import com.cotizaia.pricing.PrecioPorHoras;
import com.cotizaia.repository.RepositorioPropuestasMemoria;
import com.cotizaia.service.CotizacionFacade;
import com.cotizaia.workflow.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PatronesTest {
    @Test void dineroExactoSinDouble() {
        var precio = new PrecioPorHoras();
        assertEquals(new BigDecimal("0.30"), precio.calcular(
                List.of(new ItemEstimado("Tarea", 3)), new BigDecimal("0.10")));
        assertThrows(IllegalArgumentException.class, () -> precio.calcular(List.of(), BigDecimal.ONE));
    }
    @Test void validacionDetieneCadena() {
        AtomicBoolean ejecutado = new AtomicBoolean();
        HandlerBrief cadena = new ValidarBrief(c -> ejecutado.set(true));
        assertThrows(IllegalArgumentException.class, () -> cadena.procesar(
                new ContextoBrief(new Brief("Cliente", "   ", BigDecimal.TEN))));
        assertFalse(ejecutado.get());
    }
    @Test void reglasSinDuplicadosYAcentos() {
        AnalizadorBrief analizador = new AdaptadorReglasLocales(new MotorReglasLocales());
        assertEquals(List.of("Página web", "Menú digital", "Reservas", "Pagos"),
                analizador.detectarRequisitos("PÁGINA web, MENÚ menú, reservas y pagos"));
        assertTrue(analizador.detectarRequisitos("Revisar el apartamento").isEmpty());
    }
    @Test void analizadorSustituibleSinModificarFachada() {
        AnalizadorBrief otro = new AnalizadorBrief() {
            public List<String> detectarRequisitos(String texto) { return List.of("Catálogo"); }
            public List<String> identificarAmbiguedades(String texto) { return List.of("¿Cuántos productos?"); }
        };
        var historial = new HistorialEventos();
        var facade = fachada(otro, historial);
        var p = facade.procesar(new Brief("Cliente", "Un catálogo de productos", new BigDecimal("10")));
        assertEquals(16, p.getHorasTotales());
        assertEquals(new BigDecimal("160.00"), p.getPrecioTotal());
        facade.decidir(p.getId(), true, "Revisor", "Alcance revisado");
        assertEquals(1, historial.dePropuesta(p.getId()).size());
    }
    @Test void soloUnaDecisionConcurrenteProduceEvento() throws Exception {
        var historial = new HistorialEventos();
        var facade = fachada(new AdaptadorReglasLocales(new MotorReglasLocales()), historial);
        var p = facade.procesar(new Brief("Cliente", "Necesito una página", BigDecimal.TEN));
        CountDownLatch inicio = new CountDownLatch(1);
        try (var executor = Executors.newFixedThreadPool(2)) {
            Callable<Boolean> aprobar = () -> {
                inicio.await();
                try { facade.decidir(p.getId(), true, "Revisor", "Revisado"); return true; }
                catch (TransicionInvalida e) { return false; }
            };
            var a = executor.submit(aprobar);
            var b = executor.submit(aprobar);
            inicio.countDown();
            assertNotEquals(a.get(5, TimeUnit.SECONDS), b.get(5, TimeUnit.SECONDS));
        }
        assertEquals(1, historial.dePropuesta(p.getId()).size());
    }
    private CotizacionFacade fachada(AnalizadorBrief analizador, HistorialEventos historial) {
        HandlerBrief cadena = new ValidarBrief(new DetectarRequisitos(analizador,
                new IdentificarAmbiguedades(analizador, new EstimarHoras())));
        return new CotizacionFacade(cadena, new PrecioPorHoras(),
                new RepositorioPropuestasMemoria(), new PublicadorEventos(List.of(historial)));
    }
}
