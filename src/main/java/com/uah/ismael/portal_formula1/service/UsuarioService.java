package com.uah.ismael.portal_formula1.service;

import com.uah.ismael.portal_formula1.dto.UsuarioDTO;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UsuarioService {

    void addUsuario(UsuarioDTO usuario);
    String encryptPassword(String rawPassword);
    UsuarioDTO findByNombreUsuario(String nombreUsuario);
    boolean verifyCredentials(String nombreUsuario, String contrasena);
}
