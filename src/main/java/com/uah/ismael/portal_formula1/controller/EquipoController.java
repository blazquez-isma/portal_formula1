package com.uah.ismael.portal_formula1.controller;

import com.uah.ismael.portal_formula1.dto.EquipoDTO;
import com.uah.ismael.portal_formula1.dto.UsuarioDTO;
import com.uah.ismael.portal_formula1.utils.Constants;
import com.uah.ismael.portal_formula1.utils.PageUtil;
import com.uah.ismael.portal_formula1.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.net.MalformedURLException;
import java.security.Principal;


@Controller
@RequestMapping("/equipos")
public class EquipoController {

    Logger logger = LoggerFactory.getLogger(EquipoController.class);

    @Autowired
    private EquipoService equipoService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private CocheService cocheService;

    @Autowired
    private PilotoService pilotoService;

    @Autowired
    private UploadFileService uploadFileService;


    @GetMapping
    public String verEquipos(@RequestParam(defaultValue = "0") int page,
                             @RequestParam(defaultValue = Constants.DEFAULT_SIZE) int size,
                             @RequestParam(defaultValue = "titulo") String sortField,
                             @RequestParam(defaultValue = "asc") String sortDir,
                             Model model) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortDir), sortField));
        Page<EquipoDTO> equiposPage = equipoService.getAllEquipos(pageable);
        model.addAttribute("titulo", "Listado de Equipos");
        PageUtil.addPaginationAttributes(model, equiposPage, page, sortField, sortDir);
        return "equipos/listEquipos";
    }

    @GetMapping("/verEquipo/{idEquipo}")
    public String verEquipo(@PathVariable("idEquipo") Long idEquipo, Model model, Principal principal) {
        EquipoDTO equipo = equipoService.getEquipoById(idEquipo);
        if(equipo == null) {
            return "redirect:/equipos";
        }
        if(usuarioService.hasEditPermissions(principal, equipo)){
            model.addAttribute("puedeEditar", true);
        } else {
            model.addAttribute("puedeEditar", false);
        }
        model.addAttribute("titulo", "Equipo: " + equipo.getNombre());
        model.addAttribute("equipo", equipo);
        model.addAttribute("responsables", usuarioService.getUsuariosByEquipoId(equipo.getId()));
        model.addAttribute("coches", cocheService.getCochesByEquipoId(equipo.getId()));
        model.addAttribute("pilotos", pilotoService.getPilotosByEquipoId(equipo.getId()));
        return "equipos/seeEquipo";
    }

    @GetMapping("/verEquipoDe/{nombreUsuario}")
    public String verEquipoDe(@PathVariable("nombreUsuario") String nombreUsuario, Model model, RedirectAttributes redirectAttributes) {
        UsuarioDTO usuario = usuarioService.getUsuarioByNombreUsuario(nombreUsuario);
        if(usuario != null) {
            if(usuario.getEquipo() == null) {
                redirectAttributes.addFlashAttribute("error", usuario.getNombreUsuario() + ", no pertenece a ningún equipo");
                return "redirect:/equipos";
            }
            EquipoDTO equipo = equipoService.getEquipoById(usuario.getEquipo().getId());
            if (equipo == null) {
                redirectAttributes.addFlashAttribute("error", usuario.getNombreUsuario() + ", no pertenece a ningún equipo");
                return "redirect:/equipos";
            }
            model.addAttribute("titulo", "Equipo de " + usuario.getNombre() + ": " + equipo.getNombre());
            model.addAttribute("equipo", equipo);
//            model.addAttribute("responsables", usuarioService.getUsuariosByEquipoId(equipo.getId()));
        } else {
            redirectAttributes.addFlashAttribute("error", "No se ha encontrado el usuario con nombre de usuario " + nombreUsuario);
            return "redirect:/equipos";
        }

        return "redirect:/equipos/verEquipo/" + usuario.getEquipo().getId();
    }

    @GetMapping("/crearEquipo")
    public String crearEquipo(Model model, Principal principal, RedirectAttributes attributes) {
        if(principal == null || principal.getName() == null) {
            return "redirect:/login";
        }
        UsuarioDTO usuario = usuarioService.getUsuarioByNombreUsuario(principal.getName());
        if (usuario.getEquipo() != null) {
            attributes.addFlashAttribute("error", "Ya perteneces al equipo " + usuario.getEquipo().getNombre());
            return "redirect:/equipos/verEquipo/" + usuario.getEquipo().getId();
        }
        model.addAttribute("titulo", "Crear Equipo para " + principal.getName());
        model.addAttribute("equipo", new EquipoDTO());
        return "equipos/createEquipo";
    }

    @PostMapping("/guardarEquipo")
    public String guardarEquipo(@ModelAttribute("equipo") EquipoDTO equipo, Model model,
                                @RequestParam("file") MultipartFile logo,  RedirectAttributes attributes, Principal principal) {

        if(logo != null && !logo.isEmpty()) {
            if (equipo.getId() != null && equipo.getId() > 0 && equipo.getLogo() != null
                    && !equipo.getLogo().isEmpty()) {
                uploadFileService.delete(equipo.getLogo(), Constants.EQUIPOS);
            }
            String nombreImagen = null;
            try {
                nombreImagen = uploadFileService.copy(logo, Constants.EQUIPOS);
            } catch (IOException e) {
                e.printStackTrace();
            }
            attributes.addFlashAttribute("success", "Has subido correctamente '" + nombreImagen + "'");
            equipo.setLogo(nombreImagen);
        }

        try {
            if(equipo.getId() != null && equipo.getId() > 0) {
                if(equipoService.updateEquipo(equipo)) {
                    attributes.addFlashAttribute("success", "Equipo actualizado correctamente");
                } else {
                    attributes.addFlashAttribute("error", "No se ha podido actualizar el equipo");
                }
            } else {
                equipo = equipoService.addEquipo(equipo);
                //Actualizar el usuario con el equipo
                UsuarioDTO usuario = usuarioService.getUsuarioByNombreUsuario(principal.getName());
                usuario.setEquipo(equipo);
                usuarioService.updateUsuario(usuario);
                attributes.addFlashAttribute("success", "Equipo creado correctamente");
            }
        } catch (IllegalArgumentException e) {
            attributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/equipos";
    }

    @GetMapping("/editarEquipo/{idEquipo}")
    public String editarEquipo(@PathVariable("idEquipo") Long idEquipo, Model model, Principal principal, RedirectAttributes attributes) {
        EquipoDTO equipo = equipoService.getEquipoById(idEquipo);
        if(!usuarioService.hasEditPermissions(principal, equipo)){
            attributes.addFlashAttribute("error", "No tienes permisos para editar o borrar este equipo");
            return "redirect:/equipos/verEquipo/" + idEquipo;
        }
        model.addAttribute("titulo", "Editar Equipo");
        model.addAttribute("equipo", equipo);
        return "equipos/createEquipo";
    }

    @GetMapping("/borrarEquipo/{idEquipo}")
    public String borrarEquipo(@PathVariable("idEquipo") Long idEquipo, Principal principal, RedirectAttributes attributes) {
        EquipoDTO equipo = equipoService.getEquipoById(idEquipo);
        if(!usuarioService.hasEditPermissions(principal, equipo)){
            attributes.addFlashAttribute("error", "No tienes permisos para editar o borrar este equipo");
            return "redirect:/equipos/verEquipo/" + idEquipo;
        }
        if(equipo != null) {
            if(equipo.getLogo() != null && !equipo.getLogo().isEmpty()) {
                uploadFileService.delete(equipo.getLogo(), Constants.EQUIPOS);
            }
            equipoService.deleteEquipo(idEquipo);
            attributes.addFlashAttribute("success", "Equipo '" + equipo.getNombre() + "' eliminado correctamente");
        } else {
            attributes.addFlashAttribute("error", "No se ha encontrado el equipo con id " + idEquipo);
        }
        return "redirect:/equipos";
    }

    @GetMapping("/{idEquipo}/nuevoResponsable/{idUsuario}")
    public String nuevoResponsable(@PathVariable("idEquipo") Long idEquipo, @PathVariable("idUsuario") Long idUsuario, RedirectAttributes attributes) {
        EquipoDTO equipo = equipoService.getEquipoById(idEquipo);
        UsuarioDTO usuario = usuarioService.getUsuarioById(idUsuario);
        if(equipo != null && usuario != null) {
            usuario.setEquipo(equipo);
            usuarioService.updateUsuario(usuario);
            attributes.addFlashAttribute("success", "Responsable asignado correctamente");
        } else {
            attributes.addFlashAttribute("error", "No se ha encontrado el equipo o el usuario con los ids proporcionados");
        }
        return "redirect:/equipos";
    }

    @RequestMapping(value = "/{idEquipo}/quitarResponsable/{idUsuario}", method = {RequestMethod.GET, RequestMethod.POST})
    public String quitarResponsable(@PathVariable("idEquipo") Long idEquipo, @PathVariable("idUsuario") Long idUsuario, RedirectAttributes attributes) {
        EquipoDTO equipo = equipoService.getEquipoById(idEquipo);
        UsuarioDTO usuario = usuarioService.getUsuarioById(idUsuario);
        if(equipo != null && usuario != null) {
            usuario.setEquipo(null);
            usuarioService.updateUsuario(usuario);
            attributes.addFlashAttribute("success", "Responsable eliminado correctamente");
        } else {
            attributes.addFlashAttribute("error", "No se ha encontrado el equipo o el usuario con los ids proporcionados");
        }
        return "redirect:/equipos";
    }

    @GetMapping("/buscarResponsablesParaEquipo/{idEquipo}")
    public String buscarResponsablesSinEquipo(@PathVariable("idEquipo") Long idEquipo, Model model, Principal principal, RedirectAttributes attributes) {
        EquipoDTO equipo = equipoService.getEquipoById(idEquipo);
        if(!usuarioService.hasEditPermissions(principal, equipo)){
            attributes.addFlashAttribute("error", "No tienes permisos para editar o borrar este equipo");
            return "redirect:/equipos/verEquipo/" + idEquipo;
        }
        return "redirect:/usuarios/buscarResponsablesParaEquipo/" + idEquipo;
    }

    @GetMapping("/verImagen/{filename}")
    public ResponseEntity<Resource> verImagen(@PathVariable String filename) {
        Resource recurso = null;
        try {
            recurso = uploadFileService.load(filename, Constants.EQUIPOS);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + recurso.getFilename() + "\"")
                .body(recurso);
    }

}
