package com.uah.ismael.portal_formula1.service.impl;

import com.uah.ismael.portal_formula1.dto.UsuarioDTO;
import com.uah.ismael.portal_formula1.model.entity.Usuario;
import com.uah.ismael.portal_formula1.model.repository.UsuarioRepository;
import com.uah.ismael.portal_formula1.service.UsuarioService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    Logger LOG = LoggerFactory.getLogger(UsuarioServiceImpl.class);

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private UsuarioRepository usuarioRepository;


    @Override
    public UsuarioDTO addUsuario(UsuarioDTO usuario) {
        LOG.debug("USER: " + usuario);
        Usuario usuarioEntity = modelMapper.map(usuario, Usuario.class);
        Usuario usuarioSaved = usuarioRepository.save(usuarioEntity);
        return modelMapper.map(usuarioSaved, UsuarioDTO.class);
    }
}
