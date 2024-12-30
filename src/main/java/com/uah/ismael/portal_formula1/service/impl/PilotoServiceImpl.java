package com.uah.ismael.portal_formula1.service.impl;

import com.uah.ismael.portal_formula1.dto.PilotoDTO;
import com.uah.ismael.portal_formula1.service.PilotoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class PilotoServiceImpl implements PilotoService {
    @Override
    public void addPiloto(PilotoDTO piloto) {

    }

    @Override
    public boolean updatePiloto(PilotoDTO piloto) {
        return false;
    }

    @Override
    public void deletePiloto(Long id) {

    }

    @Override
    public Page<PilotoDTO> getAllPilotos(Pageable pageable) {
        return null;
    }

    @Override
    public PilotoDTO getPilotoById(Long id) {
        return null;
    }

    @Override
    public Page<PilotoDTO> getPilotoByNombre(Pageable pageable, String nombre) {
        return null;
    }

    @Override
    public Page<PilotoDTO> getPilotoByApellido(Pageable pageable, String apellidos) {
        return null;
    }

    @Override
    public Page<PilotoDTO> getPilotoByNombreAndApellidos(Pageable pageable, String nombre, String apellidos) {
        return null;
    }

    @Override
    public PilotoDTO getPilotoBySiglas(String siglas) {
        return null;
    }

    @Override
    public PilotoDTO getPilotoByDorsal(Integer dorsal) {
        return null;
    }

    @Override
    public Page<PilotoDTO> getPilotoByPais(Pageable pageable, String pais) {
        return null;
    }

    @Override
    public PilotoDTO getPilotoByTwitter(String twitter) {
        return null;
    }

    @Override
    public Page<PilotoDTO> getPilotoByEquipoId(Pageable pageable, Long equipoId) {
        return null;
    }
}
