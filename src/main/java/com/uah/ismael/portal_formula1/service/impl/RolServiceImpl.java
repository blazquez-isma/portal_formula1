package com.uah.ismael.portal_formula1.service.impl;

import com.uah.ismael.portal_formula1.dto.RolDTO;
import com.uah.ismael.portal_formula1.model.entity.Rol;
import com.uah.ismael.portal_formula1.model.repository.RolRepository;
import com.uah.ismael.portal_formula1.service.RolService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RolServiceImpl implements RolService {
    Logger LOG = LoggerFactory.getLogger(RolServiceImpl.class);

    private final RolRepository rolRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public RolServiceImpl(RolRepository rolRepository, ModelMapper modelMapper) {
        this.rolRepository = rolRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<RolDTO> getAllRoles() {
        List<Rol> roles = rolRepository.findAll();
        return roles.stream().map(rol -> modelMapper.map(rol, RolDTO.class)).collect(Collectors.toList());
    }

    @Override
    public RolDTO getRolById(Long id) {
        Rol rol = rolRepository.findById(id).orElse(null);
        return modelMapper.map(rol, RolDTO.class);
    }

    @Override
    public RolDTO getRolByNombre(String nombre) {
        Rol rol = rolRepository.findByNombre(nombre);
        return modelMapper.map(rol, RolDTO.class);
    }
}
