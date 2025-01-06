package com.uah.ismael.portal_formula1.service.impl;

import com.uah.ismael.portal_formula1.dto.VotacionDTO;
import com.uah.ismael.portal_formula1.model.entity.Votacion;
import com.uah.ismael.portal_formula1.model.repository.VotacionRepository;
import com.uah.ismael.portal_formula1.service.VotacionService;
import com.uah.ismael.portal_formula1.utils.PageUtil;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class VotacionServiceImpl implements VotacionService {

    Logger LOG = LoggerFactory.getLogger(VotacionServiceImpl.class);

    private final ModelMapper modelMapper;
    private final VotacionRepository votacionRepository;

    @Autowired
    public VotacionServiceImpl(ModelMapper modelMapper, VotacionRepository votacionRepository) {
        this.modelMapper = modelMapper;
        this.votacionRepository = votacionRepository;
    }

    @Override
    public void addVotacion(VotacionDTO votacionDTO) {
        Votacion votacion = votacionRepository.save(modelMapper.map(votacionDTO, Votacion.class));

        votacion.setPermalink(generatePermalinkById(votacion.getId()));
        votacionRepository.save(votacion);
    }

    @Override
    public boolean updateVotacion(VotacionDTO votacionDTO) {
        Votacion votacionToUpdate = votacionRepository.findById(votacionDTO.getId()).orElse(null);
        if (votacionToUpdate != null) {
            if (votacionDTO.getTitulo() != null && !votacionDTO.getTitulo().equals(votacionToUpdate.getTitulo())) {
                votacionToUpdate.setTitulo(votacionDTO.getTitulo());
            }
            if (votacionDTO.getDescripcion() != null && !votacionDTO.getDescripcion().equals(votacionToUpdate.getDescripcion())) {
                votacionToUpdate.setDescripcion(votacionDTO.getDescripcion());
            }
            if (votacionDTO.getFechaLimite() != null && !votacionDTO.getFechaLimite().equals(votacionToUpdate.getFechaLimite())) {
                votacionToUpdate.setFechaLimite(votacionDTO.getFechaLimite());
            }
            votacionRepository.save(votacionToUpdate);
            return true;
        }
        return false;
    }

    @Override
    public void deleteVotacion(Long idVotacion) {
        Votacion votacion = votacionRepository.findById(idVotacion).orElse(null);
        if (votacion != null) {
            votacionRepository.delete(votacion);
        }
    }

    @Override
    public Page<VotacionDTO> getAllVotaciones(Pageable pageable){
        List<VotacionDTO> votaciones = votacionRepository.findAll().stream()
                .map(votacion -> modelMapper.map(votacion, VotacionDTO.class))
                .toList();

        return PageUtil.sortedPageImpl(pageable, votaciones);
    }

    @Override
    public Page<VotacionDTO> getVotacionesActivas(Pageable pageable){
        List<VotacionDTO> votaciones = votacionRepository.findByFechaLimiteAfter(new Timestamp(System.currentTimeMillis())).stream()
                .map(votacion -> modelMapper.map(votacion, VotacionDTO.class))
                .toList();

        return PageUtil.sortedPageImpl(pageable, votaciones);
    }

    @Override
    public Page<VotacionDTO> getVotacionesFinalizadas(Pageable pageable){
        List<VotacionDTO> votaciones = votacionRepository.findByFechaLimiteBefore(new Timestamp(System.currentTimeMillis())).stream()
                .map(votacion -> modelMapper.map(votacion, VotacionDTO.class))
                .toList();

        return PageUtil.sortedPageImpl(pageable, votaciones);
    }

    @Override
    public VotacionDTO getVotacionById(Long idVotacion) {
        Votacion votacion = votacionRepository.findById(idVotacion).orElse(null);
        if (votacion != null) {
            return modelMapper.map(votacion, VotacionDTO.class);
        }
        return null;
    }

    @Override
    public VotacionDTO getVotacionByPermalink(String permalink) {
        Votacion votacion = votacionRepository.findByPermalink(permalink);
        if (votacion != null) {
            return modelMapper.map(votacion, VotacionDTO.class);
        }
        return null;
    }

    @Override
    public VotacionDTO getVotacionByTitulo(String titulo) {
        Votacion votacion = votacionRepository.findByTitulo(titulo);
        if (votacion != null) {
            return modelMapper.map(votacion, VotacionDTO.class);
        }
        return null;
    }

    @Override
    public List<VotacionDTO> getVotacionesByTitulo(String titulo) {
        return votacionRepository.findByTituloContainingIgnoreCase(titulo).stream()
                .map(votacion -> modelMapper.map(votacion, VotacionDTO.class))
                .toList();
    }

    private String generatePermalinkById(Long votacionId) {
        String dominio = "http://localhost:8080";
        return dominio + "/votaciones/verNoticia/" + votacionId;
    }

}
