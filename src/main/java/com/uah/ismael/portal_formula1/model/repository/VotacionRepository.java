package com.uah.ismael.portal_formula1.model.repository;

import com.uah.ismael.portal_formula1.model.entity.Votacion;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Timestamp;
import java.util.List;

public interface VotacionRepository extends JpaRepository<Votacion, Long> {

    Votacion findByPermalink(String permalink);

    Votacion findByTitulo(String titulo);

    List<Votacion> findByTituloContainingIgnoreCase(String nombre);

    List<Votacion> findByFechaLimiteBefore(Timestamp fechaLimiteBefore);

    List<Votacion> findByFechaLimiteAfter(Timestamp fechaLimiteAfter);
}