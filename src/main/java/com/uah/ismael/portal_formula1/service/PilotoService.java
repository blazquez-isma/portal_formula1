package com.uah.ismael.portal_formula1.service;

import com.uah.ismael.portal_formula1.dto.PilotoDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PilotoService {

    void addPiloto(PilotoDTO piloto);

    boolean updatePiloto(PilotoDTO piloto);

    void deletePiloto(Long id);

    Page<PilotoDTO> getAllPilotos(Pageable pageable);

    PilotoDTO getPilotoById(Long id);

    List<PilotoDTO> getPilotosByNombre(String nombre);

    List<PilotoDTO> getPilotosByApellido(String apellidos);

    List<PilotoDTO> getPilotosByNombreAndApellidos(String nombre, String apellidos);

    PilotoDTO getPilotoBySiglas(String siglas);

    PilotoDTO getPilotoByDorsal(Integer dorsal);

    List<PilotoDTO> getPilotosByPais(String pais);

    PilotoDTO getPilotoByTwitter(String twitter);

    List<PilotoDTO> getPilotosByEquipoId(Long equipoId);

    List<PilotoDTO> getPilotosByEquipoNombre(String equipoNombre);

    Page<PilotoDTO> getPilotosByEquipoId(Long equipoId, Pageable pageable);

}
