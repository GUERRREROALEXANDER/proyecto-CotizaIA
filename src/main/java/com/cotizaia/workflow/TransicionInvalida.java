package com.cotizaia.workflow;
public class TransicionInvalida extends RuntimeException {
    public TransicionInvalida(String mensaje) { super(mensaje); }
}
