package com.cotizaia.pricing;
import com.cotizaia.domain.ItemEstimado;
import java.math.BigDecimal;
import java.util.List;
public interface EstrategiaPrecio {
    BigDecimal calcular(List<ItemEstimado> desglose, BigDecimal tarifa);
}
