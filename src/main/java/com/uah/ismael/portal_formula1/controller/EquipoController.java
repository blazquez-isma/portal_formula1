package com.uah.ismael.portal_formula1.controller;

import com.uah.ismael.portal_formula1.dto.EquipoDTO;
import com.uah.ismael.portal_formula1.dto.UsuarioDTO;
import com.uah.ismael.portal_formula1.service.EquipoService;
import com.uah.ismael.portal_formula1.service.UploadFileService;
import com.uah.ismael.portal_formula1.service.UsuarioService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
public class EquipoController {

    Logger logger = LoggerFactory.getLogger(EquipoController.class);

    @Autowired
    private EquipoService equipoService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UploadFileService uploadFileService;


    @GetMapping("/equipos")
    public String verEquipos(@RequestParam(defaultValue = "0") int page,
                             @RequestParam(defaultValue = "5") int size,
                             @RequestParam(defaultValue = "titulo") String sortField,
                             @RequestParam(defaultValue = "asc") String sortDir,
                             Model model) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortDir), sortField));
        Page<EquipoDTO> equiposPage = equipoService.getAllEquipos(pageable);

        model.addAttribute("titulo", "Listado de Equipos");
        model.addAttribute("elements", equiposPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");

        return "equipos/listEquipos";
    }

    @GetMapping("/equipos/verEquipo/{id}")
    public String verEquipo(@PathVariable("id") Long id, Model model) {
        EquipoDTO equipo = equipoService.getEquipoById(id);
        model.addAttribute("titulo", "Equipo: " + equipo.getNombre());
        model.addAttribute("equipo", equipo);

        return "equipos/seeEquipo";
    }

    @GetMapping("/equipos/verEquipoDe/{idUsuario}")
    public String verEquipoDe(@PathVariable("idUsuario") Long idUsuario, Model model, RedirectAttributes redirectAttributes) {
        UsuarioDTO usuario = usuarioService.getUsuarioById(idUsuario);
        if(usuario != null) {
            EquipoDTO equipo = equipoService.getEquipoById(usuario.getEquipo().getId());
            if (equipo == null) {
                redirectAttributes.addFlashAttribute("msg", usuario.getNombreUsuario() + ", no pertenece a ningún equipo");
                return "redirect:/equipos";
            }
            model.addAttribute("titulo", "Equipo de " + usuario.getNombre() + ": " + equipo.getNombre());
            model.addAttribute("equipo", equipo);
        } else {
            redirectAttributes.addFlashAttribute("msg", "No se ha encontrado el usuario con id " + idUsuario);
            return "redirect:/equipos";
        }

        return "equipos/seeEquipo";
    }

    @GetMapping("/equipos/crearEquipo")
    public String crearEquipo(Model model) {
        model.addAttribute("titulo", "Crear Equipo");
        model.addAttribute("equipo", new EquipoDTO());
        return "equipos/createEquipo";
    }

    @PostMapping("/equipos/guardarEquipo")

}
