package com.uah.ismael.portal_formula1.model.repository;

import com.uah.ismael.portal_formula1.model.entity.Voto;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VotoRepository extends JpaRepository<Voto, Long> {

    boolean existsByNombreVotante(String nombreVotante);

    Voto findByNombreVotante(String nombreVotante);

    boolean existsByEmail(String email);

    Voto findByEmail(String email);

    boolean existsByNombreVotanteOrEmail(String nombreVotante, String email);

    Voto findByNombreVotanteAndEmail(String nombreVotante, String email);

    List<Voto> findByVotacion_Id(Long votacionId);

    List<Voto> findByVotacion_Permalink(String votacionPermalink);

}