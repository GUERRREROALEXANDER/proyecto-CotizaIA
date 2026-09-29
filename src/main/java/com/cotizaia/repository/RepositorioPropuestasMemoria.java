package com.cotizaia.repository;
import com.cotizaia.domain.Propuesta;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

/** Temporal: todas las propuestas se pierden al reiniciar. */
@Repository
public class RepositorioPropuestasMemoria implements RepositorioPropuestas {
    private final ConcurrentHashMap<UUID, Propuesta> propuestas = new ConcurrentHashMap<>();
    public void guardar(Propuesta p) { propuestas.put(p.getId(), p); }
    public Optional<Propuesta> buscar(UUID id) { return Optional.ofNullable(propuestas.get(id)); }
    public List<Propuesta> listar() {
        return propuestas.values().stream().sorted(Comparator.comparing(Propuesta::getFecha)).toList();
    }
}
