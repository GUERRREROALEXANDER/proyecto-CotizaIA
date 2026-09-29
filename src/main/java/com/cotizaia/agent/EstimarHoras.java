package com.cotizaia.agent;
import com.cotizaia.domain.ItemEstimado;
import java.util.ArrayList;

/** Fórmula docente: 4 h de análisis + 8 h por requisito distinto + 4 h de pruebas. */
public final class EstimarHoras implements HandlerBrief {
    public void procesar(ContextoBrief contexto) {
        var items = new ArrayList<ItemEstimado>();
        items.add(new ItemEstimado("Análisis del brief", 4));
        contexto.requisitos().forEach(r -> items.add(new ItemEstimado(r, 8)));
        items.add(new ItemEstimado("Pruebas y revisión", 4));
        contexto.desglose(items);
    }
}
