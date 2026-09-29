package com.cotizaia.agent;
import java.util.List;

/** Puerto de análisis: la cadena no conoce al proveedor concreto. */
public interface AnalizadorBrief {
    List<String> detectarRequisitos(String texto);
    List<String> identificarAmbiguedades(String texto);
}
