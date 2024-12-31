package com.uah.ismael.portal_formula1.controller;

import com.uah.ismael.portal_formula1.dto.UsuarioDTO;
import com.uah.ismael.portal_formula1.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UsuarioController {

    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(UsuarioController.class);

    @Autowired
    private UsuarioService usuarioService;


    @GetMapping("/usuarios")
    @PreAuthorize("hasRole('ROLE_ADMINISTRADOR')")
    public String showAllUsers(@RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "5") int size,
                               @RequestParam(defaultValue = "nombreUsuario") String sortField,
                               @RequestParam(defaultValue = "asc") String sortDir,
                               Model model) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortDir), sortField));
        Page<UsuarioDTO> usuarioPage = usuarioService.getAllUsuarios(pageable);

        model.addAttribute("titulo", "Listado de Usuarios");
        model.addAttribute("elements", usuarioPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");

        return "usuarios/showUsuarios";
    }

    @PostMapping("/usuario/activate_user")
    @PreAuthorize("hasRole('ROLE_ADMINISTRADOR')")
    public String validateUser(@RequestParam("userId") Long userId) {
        usuarioService.activateUsuario(userId);
        return "redirect:/usuarios";
    }

    @PostMapping("/usuario/delete_user")
    @PreAuthorize("hasRole('ROLE_ADMINISTRADOR')")
    public String deleteUser(@RequestParam("userId") Long userId) {
        usuarioService.deleteUsuario(userId);
        return "redirect:/usuarios";
    }
}
