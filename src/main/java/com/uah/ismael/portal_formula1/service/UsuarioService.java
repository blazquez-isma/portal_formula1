package com.uah.ismael.portal_formula1.service;

import com.uah.ismael.portal_formula1.dto.UsuarioDTO;
import com.uah.ismael.portal_formula1.dto.UsuarioNuevoDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UsuarioService {

    void addUsuario(UsuarioNuevoDTO usuario);

    void deleteUsuario(Long userId);

    boolean updateUsuario(UsuarioDTO usuario);

    Page<UsuarioDTO> getAllUsuarios(Pageable pageable);

    List<UsuarioDTO> getUsuariosByActivo(boolean activo);

    UsuarioDTO getUsuarioById(Long userId);

    UsuarioDTO getUsuarioByNombre(String nombre);

    UsuarioDTO getUsuarioByNombreUsuario(String nombreUsuario);

    UsuarioDTO getUsuarioByEmail(String email);

    List<UsuarioDTO> getUsuariosByEquipoId(Long equipoId);

    Page<UsuarioDTO> getPageUsuariosByEquipoId(Long equipoId, Pageable pageable);

    void activateUsuario(Long userId);
}
