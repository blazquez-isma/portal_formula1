package com.uah.ismael.portal_formula1.service;

import com.uah.ismael.portal_formula1.dto.VotacionDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.sql.Timestamp;
import java.util.List;

public interface VotacionService {

    void addVotacion(VotacionDTO votacionDTO);

    boolean updateVotacion(VotacionDTO votacionDTO);

    void deleteVotacion(Long idVotacion);

    Page<VotacionDTO> getAllVotaciones(Pageable pageable);

    Page<VotacionDTO> getVotacionesActivas(Pageable pageable);

    Page<VotacionDTO> getVotacionesFinalizadas(Pageable pageable);

    VotacionDTO getVotacionById(Long idVotacion);

    VotacionDTO getVotacionByPermalink(String permalink);

    VotacionDTO getVotacionByTitulo(String titulo);

    List<VotacionDTO> getVotacionesByTitulo(String titulo);

}
