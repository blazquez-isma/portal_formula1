package com.uah.ismael.portal_formula1.service;

import com.uah.ismael.portal_formula1.dto.PilotoDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PilotoService {

    void addPiloto(PilotoDTO piloto);

    boolean updatePiloto(PilotoDTO piloto);

    void deletePiloto(Long id);

    Page<PilotoDTO> getAllPilotos(Pageable pageable);

    PilotoDTO getPilotoById(Long id);

    Page<PilotoDTO> getPilotosByNombre(Pageable pageable, String nombre);

    Page<PilotoDTO> getPilotosByApellido(Pageable pageable, String apellidos);

    Page<PilotoDTO> getPilotosByNombreAndApellidos(Pageable pageable, String nombre, String apellidos);

    PilotoDTO getPilotoBySiglas(String siglas);

    PilotoDTO getPilotoByDorsal(Integer dorsal);

    Page<PilotoDTO> getPilotosByPais(Pageable pageable, String pais);

    PilotoDTO getPilotoByTwitter(String twitter);

    Page<PilotoDTO> getPilotosByEquipoId(Pageable pageable, Long equipoId);
}
