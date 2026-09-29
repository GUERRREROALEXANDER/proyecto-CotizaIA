package com.cotizaia.agent;

/** Cada eslabón decide si delega; una excepción impide llegar al siguiente. */
public interface HandlerBrief {
    void procesar(ContextoBrief contexto);
}
