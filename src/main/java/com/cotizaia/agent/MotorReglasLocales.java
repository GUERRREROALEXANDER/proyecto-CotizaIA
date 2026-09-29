package com.cotizaia.agent;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;
import org.springframework.stereotype.Component;

/** Motor determinista de palabras clave. No interpreta semántica ni usa IA. */
@Component
public class MotorReglasLocales {
    public enum Funcion { PAGINA_WEB, MENU, RESERVAS, PAGOS }

    public List<Funcion> buscarFunciones(String entrada) {
        String texto = normalizar(entrada);
        List<Funcion> resultado = new ArrayList<>();
        if (contiene(texto, "pagina|paginas|web|sitio")) resultado.add(Funcion.PAGINA_WEB);
        if (contiene(texto, "menu|menus|carta")) resultado.add(Funcion.MENU);
        if (contiene(texto, "reserva|reservas|reservar")) resultado.add(Funcion.RESERVAS);
        if (contiene(texto, "pago|pagos|pagar")) resultado.add(Funcion.PAGOS);
        return List.copyOf(resultado);
    }

    public List<String> buscarDudas(String entrada) {
        String texto = normalizar(entrada);
        List<Funcion> funciones = buscarFunciones(entrada);
        List<String> dudas = new ArrayList<>();
        if (funciones.isEmpty()) dudas.add("¿Qué funcionalidades concretas necesita el proyecto?");
        if (funciones.contains(Funcion.MENU)) dudas.add("¿Quién administra el menú y entrega sus contenidos?");
        if (funciones.contains(Funcion.RESERVAS)) dudas.add("¿Qué horarios, cupos y reglas de cancelación tendrán las reservas?");
        if (funciones.contains(Funcion.PAGOS)) dudas.add("¿Qué se pagará, en qué moneda y mediante qué proveedor?");
        if (contiene(texto, "diciembre")) {
            dudas.add("¿Cuál es el día y año de entrega en diciembre?");
        } else {
            dudas.add("¿Cuál es la fecha exacta de entrega y está confirmada?");
        }
        return List.copyOf(dudas);
    }

    private boolean contiene(String texto, String opciones) {
        return Pattern.compile("\\b(?:" + opciones + ")\\b").matcher(texto).find();
    }
    private String normalizar(String texto) {
        return Normalizer.normalize(texto, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "").toLowerCase(Locale.ROOT);
    }
}
