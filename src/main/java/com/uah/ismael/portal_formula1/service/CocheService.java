package com.uah.ismael.portal_formula1.service;

import com.uah.ismael.portal_formula1.dto.CocheDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CocheService {

    void addCoche(CocheDTO coche);

    boolean updateCoche(CocheDTO coche);

    void deleteCoche(Long id);

    Page<CocheDTO> getAllCoches(Pageable pageable);

    CocheDTO getCocheById(Long id);

    CocheDTO getCocheByNombre(String nombre);

    CocheDTO getCocheByCodigo(String codigo);

    Page<CocheDTO> getCochesByEquipoId(Long idEquipo, Pageable pageable);

}
