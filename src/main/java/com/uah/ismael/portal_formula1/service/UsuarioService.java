package com.uah.ismael.portal_formula1.service;

import com.uah.ismael.portal_formula1.dto.UsuarioDTO;
import com.uah.ismael.portal_formula1.dto.UsuarioNuevoDTO;

import java.util.List;

public interface UsuarioService {
    void addUsuario(UsuarioNuevoDTO usuario);
    List<UsuarioDTO> getAllUsuarios();
    List<UsuarioDTO> getUsuariosNoActivos();
    void activateUser(Long userId);
}
