package com.cotizaia.agent;
import java.util.List;
import org.springframework.stereotype.Component;

/** Adapter: traduce los códigos internos del motor al contrato del analizador. */
@Component
public class AdaptadorReglasLocales implements AnalizadorBrief {
    private final MotorReglasLocales motor;
    public AdaptadorReglasLocales(MotorReglasLocales motor) { this.motor = motor; }
    public List<String> detectarRequisitos(String texto) {
        return motor.buscarFunciones(texto).stream().map(funcion -> switch (funcion) {
            case PAGINA_WEB -> "Página web";
            case MENU -> "Menú digital";
            case RESERVAS -> "Reservas";
            case PAGOS -> "Pagos";
        }).toList();
    }
    public List<String> identificarAmbiguedades(String texto) { return motor.buscarDudas(texto); }
}
