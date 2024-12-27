package com.uah.ismael.portal_formula1.service;

import com.uah.ismael.portal_formula1.dto.RolDTO;

import java.util.List;

public interface RolService {
    List<RolDTO> getAllRoles();

    RolDTO getRolById(Long id);

    RolDTO getRolByNombre(String nombre);
}
