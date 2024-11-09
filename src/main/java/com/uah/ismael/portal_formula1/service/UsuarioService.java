package com.uah.ismael.portal_formula1.service;

import com.uah.ismael.portal_formula1.dto.UsuarioDTO;
import com.uah.ismael.portal_formula1.dto.UsuarioNuevoDTO;

import java.util.List;

public interface UsuarioService {

    void addUsuario(UsuarioNuevoDTO usuario);

    void borrarUsuario(Long userId);

    List<UsuarioDTO> getAllUsuarios();

    List<UsuarioDTO> getUsuariosByActivo(boolean activo);

    UsuarioDTO getUsuarioById(Long userId);

    UsuarioDTO getUsuarioByNombreUsuario(String nombreUsuario);

    UsuarioDTO getUsuarioByEmail(String email);

    List<UsuarioDTO> getUsuariosByRolName(String rol);

    List<UsuarioDTO> getUsuariosByRolId(Long rolId);

    void activarUsuario(Long userId);
}
