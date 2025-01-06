package com.uah.ismael.portal_formula1.service;

import com.uah.ismael.portal_formula1.dto.VotoDTO;

import java.util.List;

public interface VotoService {

    void addVoto(VotoDTO voto);

    void deleteVoto(Long id);

    VotoDTO getVotoById(Long id);

    VotoDTO getVotoByNombreVotante(String nombreVotante);

    VotoDTO getVotoByEmailVotante(String emailVotante);

    VotoDTO getVotobyNombreVotanteAndEmailVotante(String nombreVotante, String emailVotante);

    List<VotoDTO> getVotosByVotacionId(Long idVotacion);

    List<VotoDTO> getVotosByVotacionPermalink(String permalink);

}
