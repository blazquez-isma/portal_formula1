package com.uah.ismael.portal_formula1.service;

import com.uah.ismael.portal_formula1.dto.EquipoDTO;
import com.uah.ismael.portal_formula1.dto.PilotoDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EquipoService {

    void addEquipo(EquipoDTO equipo);

    boolean updateEquipo(EquipoDTO equipo);

    void deleteEquipo(Long id);

    Page<EquipoDTO> getAllEquipos(Pageable pageable);

    EquipoDTO getEquipoById(Long id);

    EquipoDTO getEquipoByNombre(String nombre);

    EquipoDTO getEquipoByTwitter(String twitter);

    EquipoDTO addPilotoToEquipo(PilotoDTO piloto);
}
