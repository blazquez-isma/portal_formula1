package com.uah.ismael.portal_formula1.service;

import com.uah.ismael.portal_formula1.dto.UsuarioDTO;
import com.uah.ismael.portal_formula1.dto.UsuarioNuevoDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UsuarioService {

    void addUsuario(UsuarioNuevoDTO usuario);

    void borrarUsuario(Long userId);

    Page<UsuarioDTO> getAllUsuarios(Pageable pageable);

//    List<UsuarioDTO> getUsuariosByActivo(boolean activo);
//
//    UsuarioDTO getUsuarioById(Long userId);
//
//    UsuarioDTO getUsuarioByNombreUsuario(String nombreUsuario);
//
//    UsuarioDTO getUsuarioByEmail(String email);
//
//    List<UsuarioDTO> getUsuariosByRolName(String rolName);
//
//    List<UsuarioDTO> getUsuariosByRolId(Long rolId);

    void activarUsuario(Long userId);
}
