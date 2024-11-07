package com.uah.ismael.portal_formula1.service.impl;

import com.uah.ismael.portal_formula1.dto.UsuarioDTO;
import com.uah.ismael.portal_formula1.dto.UsuarioNuevoDTO;
import com.uah.ismael.portal_formula1.model.entity.Rol;
import com.uah.ismael.portal_formula1.model.entity.Usuario;
import com.uah.ismael.portal_formula1.model.repository.RolRepository;
import com.uah.ismael.portal_formula1.model.repository.UsuarioRepository;
import com.uah.ismael.portal_formula1.service.UsuarioService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class UsuarioServiceImpl implements UsuarioService {

    Logger LOG = LoggerFactory.getLogger(UsuarioServiceImpl.class);

    private final ModelMapper modelMapper;
    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UsuarioServiceImpl(ModelMapper modelMapper, UsuarioRepository usuarioRepository,
                              RolRepository rolRepository, PasswordEncoder passwordEncoder) {
        this.modelMapper = modelMapper;
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void addUsuario(UsuarioNuevoDTO usuarioDTO) {
        LOG.debug("USER: " + usuarioDTO);

        if (usuarioRepository.existsByNombreUsuario(usuarioDTO.getNombreUsuario())) {
            throw new IllegalArgumentException("Ya existe un Usuario con nombre de usuario '" + usuarioDTO.getNombreUsuario() + "'");
        }

        if (usuarioRepository.existsByEmail(usuarioDTO.getEmail())) {
            throw new IllegalArgumentException("Ya existe un Usuario con email '" + usuarioDTO.getEmail() + "'");
        }

        Usuario usuario = new Usuario();
        usuario.setNombre(usuarioDTO.getNombre());
        usuario.setNombreUsuario(usuarioDTO.getNombreUsuario());
        usuario.setEmail(usuarioDTO.getEmail());
        usuario.setContrasena(passwordEncoder.encode(usuarioDTO.getContrasena()));
        Rol rol = rolRepository.findById(usuarioDTO.getRolLeido())
                .orElseThrow(() -> new IllegalArgumentException("Invalid role ID"));
        usuario.setRoles(new HashSet<>());
        usuario.getRoles().add(rol);
        usuario.setActivo(false);

        usuarioRepository.save(usuario);
    }

    @Override
    public List<UsuarioDTO> getAllUsuarios() {
        return usuarioRepository.findAll().stream()
                .map(usuario -> modelMapper.map(usuario, UsuarioDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<UsuarioDTO> getUsuariosNoActivos() {
        return usuarioRepository.findByActivo().stream()
                .map(usuario -> modelMapper.map(usuario, UsuarioDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public void activateUser(Long userId) {
        Usuario usuario = usuarioRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));
        usuario.setActivo(true);
        usuarioRepository.save(usuario);
    }

}
