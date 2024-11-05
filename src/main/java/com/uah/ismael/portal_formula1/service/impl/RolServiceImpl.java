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
        LOG.debug(roles.size() + " roles found");
        return roles.stream().map(rol -> modelMapper.map(rol, RolDTO.class)).collect(Collectors.toList());
    }
}
