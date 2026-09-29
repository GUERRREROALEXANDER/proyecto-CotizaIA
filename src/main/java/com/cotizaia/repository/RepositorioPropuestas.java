package com.cotizaia.repository;
import com.cotizaia.domain.Propuesta;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RepositorioPropuestas {
    void guardar(Propuesta propuesta);
    Optional<Propuesta> buscar(UUID id);
    List<Propuesta> listar();
}
