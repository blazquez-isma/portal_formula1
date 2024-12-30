package com.uah.ismael.portal_formula1.model.repository;

import com.uah.ismael.portal_formula1.model.entity.Equipo;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EquipoRepository extends JpaRepository<Equipo, Long> {

    Equipo findByNombre(String nombre);

    Equipo findByTwitter(String twitter);

    boolean existsEquipoByNombre(String nombre);

    boolean existsEquipoByTwitter(String twitter);
}
