package com.uah.ismael.portal_formula1.service.impl;

import com.uah.ismael.portal_formula1.dto.UsuarioDTO;
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

import java.util.HashMap;
import java.util.HashSet;
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
    public void addUsuario(UsuarioDTO usuarioDTO) {
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
        String encode = passwordEncoder.encode(usuarioDTO.getContrasena());
        LOG.debug("Contrasena " + usuarioDTO.getContrasena() + " encoded to " + encode);
        usuario.setContrasena(encode);
        Rol rol = rolRepository.findById(usuarioDTO.getRol())
                .orElseThrow(() -> new IllegalArgumentException("Invalid role ID"));
        usuario.setRoles(new HashSet<>());
        usuario.getRoles().add(rol);

        LOG.debug("USUARIO Antes: " + usuario);
        Usuario save = usuarioRepository.save(usuario);
        LOG.debug("USUARIO Despues: " + save);
    }

    @Override
    public String encryptPassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    @Override
    public UsuarioDTO findByNombreUsuario(String nombreUsuario) {
        return modelMapper.map(usuarioRepository.findByNombreUsuario(nombreUsuario), UsuarioDTO.class);
    }

    @Override
    public boolean verifyCredentials(String nombreUsuario, String contrasena) {
        Usuario usuario = usuarioRepository.findByNombreUsuario(nombreUsuario);
        return usuario != null && passwordEncoder.matches(contrasena, usuario.getContrasena());
    }

}
