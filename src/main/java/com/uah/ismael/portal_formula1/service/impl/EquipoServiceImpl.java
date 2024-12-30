package com.uah.ismael.portal_formula1.service.impl;

import com.uah.ismael.portal_formula1.dto.EquipoDTO;
import com.uah.ismael.portal_formula1.model.entity.Equipo;
import com.uah.ismael.portal_formula1.model.repository.EquipoRepository;
import com.uah.ismael.portal_formula1.paginator.PageUtil;
import com.uah.ismael.portal_formula1.service.EquipoService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public class EquipoServiceImpl implements EquipoService {

    Logger LOG = LoggerFactory.getLogger(EquipoServiceImpl.class);

    private final ModelMapper modelMapper;
    private final EquipoRepository equipoRepository;

    public EquipoServiceImpl(ModelMapper modelMapper, EquipoRepository equipoRepository) {
        this.modelMapper = modelMapper;
        this.equipoRepository = equipoRepository;
    }

    @Override
    public void addEquipo(EquipoDTO equipo) {
        if(equipoRepository.existsEquipoByNombre(equipo.getNombre())) {
            throw new IllegalArgumentException("Ya existe un equipo con el nombre '" + equipo.getNombre() + "'");
        }
        if(equipoRepository.existsEquipoByTwitter(equipo.getTwitter())) {
            throw new IllegalArgumentException("Ya existe un equipo con el twitter '" + equipo.getTwitter() + "'");
        }
        equipoRepository.save(modelMapper.map(equipo, Equipo.class));
    }

    @Override
    public boolean updateEquipo(EquipoDTO equipo) {
        Equipo equipoToUpdate = equipoRepository.findById(equipo.getId()).orElse(null);
        if(equipoToUpdate != null){
            if (equipoRepository.existsEquipoByNombre(equipo.getNombre()) && !equipoToUpdate.getNombre().equals(equipo.getNombre())) {
                throw new IllegalArgumentException("Ya existe un equipo con el nombre '" + equipo.getNombre() + "'");
            }
            if (equipoRepository.existsEquipoByTwitter(equipo.getTwitter()) && !equipoToUpdate.getTwitter().equals(equipo.getTwitter())) {
                throw new IllegalArgumentException("Ya existe un equipo con el twitter '" + equipo.getTwitter() + "'");
            }
            if(equipo.getNombre() != null) {
                equipoToUpdate.setNombre(equipo.getNombre());
            }
            if(equipo.getTwitter() != null) {
                equipoToUpdate.setTwitter(equipo.getTwitter());
            }
            if (equipo.getLogo() != null) {
                equipoToUpdate.setLogo(equipo.getLogo());
            }
            equipoRepository.save(equipoToUpdate);
        }
        return false;
    }

    @Override
    public void deleteEquipo(Long id) {
        if(equipoRepository.existsById(id)) {
            equipoRepository.deleteById(id);
        }
    }

    @Override
    public EquipoDTO getEquipoById(Long id) {
        Equipo equipo = equipoRepository.findById(id).orElse(null);
        if(equipo != null) {
            return modelMapper.map(equipo, EquipoDTO.class);
        }
        return null;
    }

    @Override
    public Page<EquipoDTO> getAllEquipos(Pageable pageable) {
        List<EquipoDTO> equipos = equipoRepository.findAll().stream()
                .map(equipo -> modelMapper.map(equipo, EquipoDTO.class))
                .sorted(EquipoDTO.getEquipoPageableComparator(pageable))
                .toList();

        return PageUtil.sortedPageImpl(pageable, equipos);
    }

    @Override
    public EquipoDTO getEquipoByNombre(String nombre) {
        Equipo equipo = equipoRepository.findByNombre(nombre);
        if(equipo != null) {
            return modelMapper.map(equipo, EquipoDTO.class);
        }
        return null;
    }

    @Override
    public EquipoDTO getEquipoByTwitter(String twitter) {
        Equipo equipo = equipoRepository.findByTwitter(twitter);
        if(equipo != null) {
            return modelMapper.map(equipo, EquipoDTO.class);
        }
        return null;
    }
}
