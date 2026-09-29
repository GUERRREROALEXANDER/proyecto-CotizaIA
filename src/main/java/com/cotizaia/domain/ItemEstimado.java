package com.cotizaia.domain;

public record ItemEstimado(String tarea, int horas) {
    public ItemEstimado {
        if (tarea == null || tarea.isBlank() || horas <= 0) {
            throw new IllegalArgumentException("Cada tarea requiere nombre y horas positivas");
        }
    }
}
