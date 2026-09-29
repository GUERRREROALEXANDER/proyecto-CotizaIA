package com.cotizaia.agent;

public final class DetectarRequisitos implements HandlerBrief {
    private final AnalizadorBrief analizador;
    private final HandlerBrief siguiente;
    public DetectarRequisitos(AnalizadorBrief analizador, HandlerBrief siguiente) {
        this.analizador = analizador; this.siguiente = siguiente;
    }
    public void procesar(ContextoBrief contexto) {
        contexto.requisitos(analizador.detectarRequisitos(contexto.brief().texto()));
        siguiente.procesar(contexto);
    }
}
