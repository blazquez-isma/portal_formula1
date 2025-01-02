package com.uah.ismael.portal_formula1.controller;

import com.uah.ismael.portal_formula1.dto.UsuarioDTO;
import com.uah.ismael.portal_formula1.paginator.PageUtil;
import com.uah.ismael.portal_formula1.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(UsuarioController.class);

    @Autowired
    private UsuarioService usuarioService;


    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMINISTRADOR')")
    public String showAllUsers(@RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "5") int size,
                               @RequestParam(defaultValue = "nombreUsuario") String sortField,
                               @RequestParam(defaultValue = "asc") String sortDir,
                               Model model) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortDir), sortField));
        Page<UsuarioDTO> usuarioPage = usuarioService.getAllUsuarios(pageable);

        model.addAttribute("titulo", "Listado de Usuarios");
        PageUtil.addPaginationAttributes(model, usuarioPage, page, sortField, sortDir);

        return "usuarios/showUsuarios";
    }

    @PostMapping("/activarUsuario")
    @PreAuthorize("hasRole('ROLE_ADMINISTRADOR')")
    public String validateUser(@RequestParam("userId") Long userId) {
        usuarioService.activateUsuario(userId);
        return "redirect:/usuarios";
    }

    @PostMapping("/borrarUsuario")
    @PreAuthorize("hasRole('ROLE_ADMINISTRADOR')")
    public String deleteUser(@RequestParam("userId") Long userId) {
        usuarioService.deleteUsuario(userId);
        return "redirect:/usuarios";
    }

    @GetMapping("/verResponsables/{idEquipo}")
    public String showResponsablesEquipo(@PathVariable("idEquipo") Long idEquipo,
                                         @RequestParam(defaultValue = "0") int page,
                                         @RequestParam(defaultValue = "5") int size,
                                         @RequestParam(defaultValue = "nombreUsuario") String sortField,
                                         @RequestParam(defaultValue = "asc") String sortDir,
                                         Model model) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortDir), sortField));
        Page<UsuarioDTO> usuarioPage = usuarioService.getPageUsuariosByEquipoId(idEquipo, pageable);

        model.addAttribute("titulo", "Listado de Responsables de Equipo");
        PageUtil.addPaginationAttributes(model, usuarioPage, page, sortField, sortDir);
        return "usuarios/showUsuarios";
    }


}
