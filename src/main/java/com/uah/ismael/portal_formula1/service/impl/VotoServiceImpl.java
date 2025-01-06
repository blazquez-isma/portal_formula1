package com.uah.ismael.portal_formula1.service.impl;

import com.uah.ismael.portal_formula1.dto.VotoDTO;
import com.uah.ismael.portal_formula1.model.entity.Voto;
import com.uah.ismael.portal_formula1.model.repository.VotoRepository;
import com.uah.ismael.portal_formula1.service.VotoService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VotoServiceImpl implements VotoService {

    Logger LOG = LoggerFactory.getLogger(VotoServiceImpl.class);

    private final ModelMapper modelMapper;
    private final VotoRepository votoRepository;

    @Autowired
    public VotoServiceImpl(ModelMapper modelMapper, VotoRepository votoRepository) {
        this.modelMapper = modelMapper;
        this.votoRepository = votoRepository;
    }

    @Override
    public void addVoto(VotoDTO voto) {
        if(votoRepository.existsByNombreVotante(voto.getNombreVotante())) {
            throw new IllegalArgumentException("Ya existe un voto a nombre de '" + voto.getNombreVotante() + "'");
        }
        if(votoRepository.existsByEmail(voto.getEmail())) {
            throw new IllegalArgumentException("Ya existe un voto con email '" + voto.getEmail() + "'");
        }
        votoRepository.save(modelMapper.map(voto, Voto.class));
    }

    @Override
    public void deleteVoto(Long id) {
        if(votoRepository.existsById(id)) {
            votoRepository.deleteById(id);
        }
    }

    @Override
    public VotoDTO getVotoById(Long id) {
        Voto voto = votoRepository.findById(id).orElse(null);
        if (voto != null) {
            return modelMapper.map(voto, VotoDTO.class);
        }
        return null;
    }

    @Override
    public VotoDTO getVotoByNombreVotante(String nombreVotante) {
        Voto voto = votoRepository.findByNombreVotante(nombreVotante);
        if (voto != null) {
            return modelMapper.map(voto, VotoDTO.class);
        }
        return null;
    }

    @Override
    public VotoDTO getVotoByEmailVotante(String emailVotante) {
        Voto voto = votoRepository.findByEmail(emailVotante);
        if (voto != null) {
            return modelMapper.map(voto, VotoDTO.class);
        }
        return null;
    }

    @Override
    public VotoDTO getVotobyNombreVotanteAndEmailVotante(String nombreVotante, String emailVotante) {
        Voto voto = votoRepository.findByNombreVotanteAndEmail(nombreVotante, emailVotante);
        if (voto != null) {
            return modelMapper.map(voto, VotoDTO.class);
        }
        return null;
    }

    @Override
    public List<VotoDTO> getVotosByVotacionId(Long idVotacion) {
        return votoRepository.findByVotacion_Id(idVotacion).stream()
                .map(voto -> modelMapper.map(voto, VotoDTO.class))
                .toList();
    }

    @Override
    public List<VotoDTO> getVotosByVotacionPermalink(String permalink) {
        return votoRepository.findByVotacion_Permalink(permalink).stream()
                .map(voto -> modelMapper.map(voto, VotoDTO.class))
                .toList();
    }
}
