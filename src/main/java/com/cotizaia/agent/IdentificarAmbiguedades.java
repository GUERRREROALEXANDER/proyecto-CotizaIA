package com.cotizaia.agent;

public final class IdentificarAmbiguedades implements HandlerBrief {
    private final AnalizadorBrief analizador;
    private final HandlerBrief siguiente;
    public IdentificarAmbiguedades(AnalizadorBrief analizador, HandlerBrief siguiente) {
        this.analizador = analizador; this.siguiente = siguiente;
    }
    public void procesar(ContextoBrief contexto) {
        contexto.preguntas(analizador.identificarAmbiguedades(contexto.brief().texto()));
        siguiente.procesar(contexto);
    }
}
