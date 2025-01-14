package com.uah.ismael.portal_formula1.model.repository;

import com.uah.ismael.portal_formula1.model.entity.Circuito;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Date;
import java.util.List;


public interface CircuitoRepository extends JpaRepository<Circuito, Long> {

    Circuito findByNombre(String nombre);

    List<Circuito> findByNombreContainingIgnoreCase(String nombre);

    List<Circuito> findByCiudadContainingIgnoreCase(String ciudad);

    List<Circuito> findByPaisContainingIgnoreCase(String pais);

    List<Circuito> findByTrazadoContainingIgnoreCase(String trazado);

    List<Circuito> findByFechaCalendario(Date fechaCalendario);

    List<Circuito> findByFechaCalendarioIsNull();

    List<Circuito> findByFechaCalendarioIsNotNull();

    boolean existsByNombre(String nombre);

    boolean existsByFechaCalendario(Date fechaCalendario);
}
