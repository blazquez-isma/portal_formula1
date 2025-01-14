package com.uah.ismael.portal_formula1.model.repository;

import com.uah.ismael.portal_formula1.model.entity.Voto;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VotoRepository extends JpaRepository<Voto, Long> {

    boolean existsByNombreVotanteAndVotacion_Id(String nombreVotante, Long votacionId);

    Voto findByNombreVotante(String nombreVotante);

    boolean existsByEmailAndVotacion_Id(String email, Long votacionId);

    Voto findByEmail(String email);

    boolean existsByNombreVotanteOrEmailAndVotacion_Id(String nombreVotante, String email, Long votacionId);

    Voto findByNombreVotanteAndEmail(String nombreVotante, String email);

    List<Voto> findByVotacion_Id(Long votacionId);

    List<Voto> findByVotacion_Permalink(String votacionPermalink);

}