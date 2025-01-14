package com.uah.ismael.portal_formula1.service;

import com.uah.ismael.portal_formula1.dto.CircuitoDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CircuitoService {

    void addCircuito(CircuitoDTO circuito);

    boolean updateCircuito(CircuitoDTO circuito);

    void deleteCircuito(Long id);

    CircuitoDTO getCircuitoById(Long id);

    Page<CircuitoDTO> getAllCircuitos(Pageable pageable);

    List<CircuitoDTO> getCircuitosFechaNotNull();

    List<CircuitoDTO> getCircuitosFechaNull();
}
