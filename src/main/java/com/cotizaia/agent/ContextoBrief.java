package com.cotizaia.agent;
import com.cotizaia.domain.Brief;
import com.cotizaia.domain.ItemEstimado;
import java.util.List;

/** Una instancia por petición: los handlers no comparten resultados entre usuarios. */
public class ContextoBrief {
    private final Brief brief;
    private List<String> requisitos = List.of();
    private List<String> preguntas = List.of();
    private List<ItemEstimado> desglose = List.of();
    public ContextoBrief(Brief brief) { this.brief = brief; }
    public Brief brief() { return brief; }
    public List<String> requisitos() { return requisitos; }
    public List<String> preguntas() { return preguntas; }
    public List<ItemEstimado> desglose() { return desglose; }
    public void requisitos(List<String> v) { requisitos = List.copyOf(v); }
    public void preguntas(List<String> v) { preguntas = List.copyOf(v); }
    public void desglose(List<ItemEstimado> v) { desglose = List.copyOf(v); }
}
