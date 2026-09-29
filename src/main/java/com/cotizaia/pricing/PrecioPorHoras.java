package com.cotizaia.pricing;
import com.cotizaia.domain.ItemEstimado;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class PrecioPorHoras implements EstrategiaPrecio {
    public BigDecimal calcular(List<ItemEstimado> desglose, BigDecimal tarifa) {
        if (tarifa == null || tarifa.signum() <= 0 || desglose == null || desglose.isEmpty()) {
            throw new IllegalArgumentException("Se requieren tarifa positiva y tareas");
        }
        long horas = desglose.stream().mapToLong(ItemEstimado::horas).sum();
        return tarifa.multiply(BigDecimal.valueOf(horas)).setScale(2, RoundingMode.HALF_UP);
    }
}
