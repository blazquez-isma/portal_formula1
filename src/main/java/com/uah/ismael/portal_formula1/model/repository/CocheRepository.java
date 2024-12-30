package com.uah.ismael.portal_formula1.model.repository;

import com.uah.ismael.portal_formula1.model.entity.Coche;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CocheRepository extends JpaRepository<Coche, Long> {

    Coche findByNombre(String nombre);

    Coche findByCodigo(String codigo);

    List<Coche> findByEquipo_Id(Long equipoId);

    boolean existsCocheByNombre(String nombre);

    boolean existsCocheByCodigo(String codigo);
}

