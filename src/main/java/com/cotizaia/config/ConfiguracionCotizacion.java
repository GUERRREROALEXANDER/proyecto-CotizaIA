package com.cotizaia.config;
import com.cotizaia.agent.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfiguracionCotizacion {
    @Bean
    HandlerBrief cadenaBrief(AnalizadorBrief analizador) {
        return new ValidarBrief(new DetectarRequisitos(analizador,
                new IdentificarAmbiguedades(analizador, new EstimarHoras())));
    }
}
