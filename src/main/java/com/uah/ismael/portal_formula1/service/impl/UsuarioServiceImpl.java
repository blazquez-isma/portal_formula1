package com.uah.ismael.portal_formula1.service.impl;

import com.uah.ismael.portal_formula1.dto.UsuarioDTO;
import com.uah.ismael.portal_formula1.dto.UsuarioNuevoDTO;
import com.uah.ismael.portal_formula1.model.entity.Rol;
import com.uah.ismael.portal_formula1.model.entity.Usuario;
import com.uah.ismael.portal_formula1.model.repository.RolRepository;
import com.uah.ismael.portal_formula1.model.repository.UsuarioRepository;
import com.uah.ismael.portal_formula1.paginator.PageUtil;
import com.uah.ismael.portal_formula1.service.UsuarioService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
        usuario.setRoles(new ArrayList<>());
        usuario.getRoles().add(rol);
        usuario.setActivo(false);

        usuarioRepository.save(usuario);
    }

    @Override
    public void deleteUsuario(Long userId) {
        usuarioRepository.deleteById(userId);
    }

    @Override
    public Page<UsuarioDTO> getAllUsuarios(Pageable pageable) {

        List<UsuarioDTO> usuarios = usuarioRepository.findAll().stream()
                .map(usuario -> modelMapper.map(usuario, UsuarioDTO.class))
                //.filter(user -> !user.getNombreUsuario().equals(currentUsername))
                .peek(user -> user.getRoles().forEach(rol -> rol.setNombre(rol.getNombre().replace("ROLE_", ""))))
                .sorted(UsuarioDTO.getUsuarioPageableComparator(pageable)).collect(Collectors.toList())
                ;

        return PageUtil.sortedPageImpl(pageable, usuarios);
    }

    @Override
    public void activateUsuario(Long userId) {
        Usuario usuario = usuarioRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));
        usuario.setActivo(true);
        usuarioRepository.save(usuario);
    }

    @Override
    public List<UsuarioDTO> getUsuariosByActivo(boolean activo) {
        return usuarioRepository.findByActivo(activo).stream()
                .map(usuario -> modelMapper.map(usuario, UsuarioDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public UsuarioDTO getUsuarioById(Long userId) {
        return usuarioRepository.findById(userId)
                .map(usuario -> modelMapper.map(usuario, UsuarioDTO.class))
                .orElse(null);
    }

    @Override
    public UsuarioDTO getUsuarioByNombre(String nombre) {
        Usuario user = usuarioRepository.findByNombre(nombre);
        return user != null ? modelMapper.map(user, UsuarioDTO.class) : null;
    }

    @Override
    public UsuarioDTO getUsuarioByNombreUsuario(String nombreUsuario) {
        Usuario user = usuarioRepository.findByNombreUsuario(nombreUsuario);
        return user != null ? modelMapper.map(user, UsuarioDTO.class) : null;
    }

    @Override
    public UsuarioDTO getUsuarioByEmail(String email) {
        Usuario user = usuarioRepository.findByEmail(email);
        return user != null ? modelMapper.map(user, UsuarioDTO.class) : null;
    }

//    @Override
//    public List<UsuarioDTO> getUsuariosByRolName(String rolName) {
//        if (rolName != null) {
//            return usuarioRepository.findByRolsNombre(rolName).stream()
//                    .map(usuario -> modelMapper.map(usuario, UsuarioDTO.class))
//                    .collect(Collectors.toList());
//        }
//        return List.of();
//    }
//
//    @Override
//    public List<UsuarioDTO> getUsuariosByRolId(Long rolId) {
//        if (rolId != null) {
//            return usuarioRepository.findByRolsId(rolId).stream()
//                    .map(usuario -> modelMapper.map(usuario, UsuarioDTO.class))
//                    .collect(Collectors.toList())
//                    ;
//        }
//        return List.of();
//    }

}
