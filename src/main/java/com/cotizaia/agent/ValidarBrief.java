package com.cotizaia.agent;
import com.cotizaia.domain.Brief;

public final class ValidarBrief implements HandlerBrief {
    private final HandlerBrief siguiente;
    public ValidarBrief(HandlerBrief siguiente) { this.siguiente = siguiente; }
    public void procesar(ContextoBrief contexto) {
        Brief b = contexto.brief();
        if (b == null || b.cliente() == null || b.cliente().isBlank() || b.cliente().length() > 120
                || b.texto() == null || b.texto().strip().length() < 10 || b.texto().length() > 10000
                || b.tarifa() == null || b.tarifa().signum() <= 0
                || b.tarifa().scale() > 2 || b.tarifa().compareTo(new java.math.BigDecimal("999999999.99")) > 0) {
            throw new IllegalArgumentException("Brief inválido: cliente de 1 a 120 caracteres, texto de 10 a 10000 y tarifa positiva de hasta 9 enteros y 2 decimales");
        }
        siguiente.procesar(contexto);
    }
}
