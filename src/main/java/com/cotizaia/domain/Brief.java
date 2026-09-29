package com.cotizaia.domain;
import java.math.BigDecimal;

/** Entrada del caso de uso; la cadena también valida llamadas fuera de HTTP. */
public record Brief(String cliente, String texto, BigDecimal tarifa) { }
