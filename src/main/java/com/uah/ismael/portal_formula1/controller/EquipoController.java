package com.uah.ismael.portal_formula1.controller;

import com.uah.ismael.portal_formula1.dto.EquipoDTO;
import com.uah.ismael.portal_formula1.dto.PilotoDTO;
import com.uah.ismael.portal_formula1.dto.UsuarioDTO;
import com.uah.ismael.portal_formula1.paginator.PageUtil;
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
                             @RequestParam(defaultValue = "5") int size,
                             @RequestParam(defaultValue = "titulo") String sortField,
                             @RequestParam(defaultValue = "asc") String sortDir,
                             Model model) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortDir), sortField));
        Page<EquipoDTO> equiposPage = equipoService.getAllEquipos(pageable);

        model.addAttribute("titulo", "Listado de Equipos");
        PageUtil.addPaginationAttributes(model, equiposPage, page, sortField, sortDir);
        return "equipos/listEquipos";
    }

    @GetMapping("/verEquipo/{id}")
    public String verEquipo(@PathVariable("id") Long id, Model model) {
        EquipoDTO equipo = equipoService.getEquipoById(id);
        model.addAttribute("titulo", "Equipo: " + equipo.getNombre());
        model.addAttribute("equipo", equipo);

//        model.addAttribute("responsables", usuarioService.getUsuariosByEquipoId(equipo.getId()));
//        model.addAttribute("coches", cocheService.getCochesByEquipoId(equipo.getId()));
        return "equipos/seeEquipo";
    }

    @GetMapping("/verEquipoDe/{idUsuario}")
    public String verEquipoDe(@PathVariable("idUsuario") Long idUsuario, Model model, RedirectAttributes redirectAttributes) {
        UsuarioDTO usuario = usuarioService.getUsuarioById(idUsuario);
        if(usuario != null) {
            EquipoDTO equipo = equipoService.getEquipoById(usuario.getEquipo().getId());
            if (equipo == null) {
                redirectAttributes.addFlashAttribute("error", usuario.getNombreUsuario() + ", no pertenece a ningún equipo");
                return "redirect:/equipos";
            }
            model.addAttribute("titulo", "Equipo de " + usuario.getNombre() + ": " + equipo.getNombre());
            model.addAttribute("equipo", equipo);
//            model.addAttribute("responsables", usuarioService.getUsuariosByEquipoId(equipo.getId()));

        } else {
            redirectAttributes.addFlashAttribute("error", "No se ha encontrado el usuario con id " + idUsuario);
            return "redirect:/equipos";
        }

        return "equipos/seeEquipo";
    }

    @GetMapping("/crearEquipo")
    public String crearEquipo(Model model) {
        model.addAttribute("titulo", "Crear Equipo");
        model.addAttribute("equipo", new EquipoDTO());
        return "equipos/createEquipo";
    }

    @PostMapping("/guardarEquipo")
    public String guardarEquipo(@ModelAttribute("equipo") EquipoDTO equipo, Model model,
                                @RequestParam("file") MultipartFile logo,  RedirectAttributes attributes) {

        if(logo != null && !logo.isEmpty()) {
            if (equipo.getId() != null && equipo.getId() > 0 && equipo.getLogo() != null
                    && !equipo.getLogo().isEmpty()) {
                uploadFileService.delete(equipo.getLogo());
            }
            String nombreImagen = null;
            try {
                nombreImagen = uploadFileService.copy(logo);
            } catch (IOException e) {
                e.printStackTrace();
            }
            attributes.addFlashAttribute("success", "Has subido correctamente '" + nombreImagen + "'");
            equipo.setLogo(nombreImagen);
        }

        if(equipo.getId() != null && equipo.getId() > 0) {
            equipoService.updateEquipo(equipo);
        } else {
            equipoService.addEquipo(equipo);
        }

        return "redirect:/equipos";
    }

    @GetMapping("/editarEquipo/{id}")
    public String editarEquipo(@PathVariable("id") Long id, Model model) {
        EquipoDTO equipo = equipoService.getEquipoById(id);
        model.addAttribute("titulo", "Editar Equipo");
        model.addAttribute("equipo", equipo);
        return "equipos/createEquipo";
    }

    @GetMapping("/borrarEquipo/{id}")
    public String borrarEquipo(@PathVariable("id") Long id, RedirectAttributes attributes) {
        EquipoDTO equipo = equipoService.getEquipoById(id);
        if(equipo != null) {
            if(equipo.getLogo() != null && !equipo.getLogo().isEmpty()) {
                uploadFileService.delete(equipo.getLogo());
            }
            equipoService.deleteEquipo(id);
            attributes.addFlashAttribute("success", "Equipo '" + equipo.getNombre() + "' eliminado correctamente");
        } else {
            attributes.addFlashAttribute("error", "No se ha encontrado el equipo con id " + id);
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

    @GetMapping("/{idEquipo}/quitarResponsable/{idUsuario}")
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


    @GetMapping("/verImagen/{filename}")
    public ResponseEntity<Resource> verImagen(@PathVariable String filename) {
        Resource recurso = null;
        try {
            recurso = uploadFileService.load(filename);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + recurso.getFilename() + "\"")
                .body(recurso);
    }
}
