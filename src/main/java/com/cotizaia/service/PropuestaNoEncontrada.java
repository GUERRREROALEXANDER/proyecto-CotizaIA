package com.cotizaia.service;
import java.util.UUID;
public class PropuestaNoEncontrada extends RuntimeException {
    public PropuestaNoEncontrada(UUID id) { super("No existe la propuesta " + id); }
}
