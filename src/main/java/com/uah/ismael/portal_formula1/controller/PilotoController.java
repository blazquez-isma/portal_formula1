package com.uah.ismael.portal_formula1.controller;

import com.uah.ismael.portal_formula1.dto.EquipoDTO;
import com.uah.ismael.portal_formula1.dto.PilotoDTO;
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
import java.util.List;

@Controller
@RequestMapping("/pilotos")
public class PilotoController {

    Logger logger = LoggerFactory.getLogger(PilotoController.class);

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

    @GetMapping("/byUsuario/{nombreUsuario}")
    public String verPilotosDeEquipo(@PathVariable("nombreUsuario") String nombreUsuario,
                                     @RequestParam(defaultValue = "0") int page,
                                     @RequestParam(defaultValue = Constants.DEFAULT_SIZE) int size,
                                     @RequestParam(defaultValue = "nombre") String sortField,
                                     @RequestParam(defaultValue = "asc") String sortDir,
                                     Model model, Principal principal) {
        UsuarioDTO usuario = usuarioService.getUsuarioByNombreUsuario(nombreUsuario);
        if(usuario.getEquipo() == null) {
            model.addAttribute("error", "El usuario " + nombreUsuario + " no pertenece a ningún equipo");
            return "redirect:/equipos";
        }
        EquipoDTO equipo = usuario.getEquipo();
        return commonSeePilotos(page, size, sortField, sortDir, model, equipo, principal);
    }

    @GetMapping("/byEquipo/{idEquipo}")
    public String verPilotosDeEquipo(@PathVariable("idEquipo") Long idEquipo,
                                     @RequestParam(defaultValue = "0") int page,
                                     @RequestParam(defaultValue = Constants.DEFAULT_SIZE) int size,
                                     @RequestParam(defaultValue = "nombre") String sortField,
                                     @RequestParam(defaultValue = "asc") String sortDir,
                                     Model model, Principal principal) {
        EquipoDTO equipo = equipoService.getEquipoById(idEquipo);
        return commonSeePilotos(page, size, sortField, sortDir, model, equipo, principal);
    }

    private String commonSeePilotos(int page, int size, String sortField, String sortDir, Model model, EquipoDTO equipo, Principal principal) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortDir), sortField));
        Page<PilotoDTO> pilotosPage = pilotoService.getPilotosByEquipoId(equipo.getId(), pageable);

        if(usuarioService.hasEditPermissions(principal, equipo)) {
            model.addAttribute("puedeEditar", true);
        } else {
            model.addAttribute("puedeEditar", false);
        }

        model.addAttribute("titulo", "Pilotos de " + equipo.getNombre());
        model.addAttribute("idEquipo", equipo.getId());
        PageUtil.addPaginationAttributes(model, pilotosPage, page, sortField, sortDir);
        return "pilotos/listPilotos";
    }

    @GetMapping("/verPiloto/{idPiloto}")
    public String verPiloto(@PathVariable("idPiloto") Long idPiloto, Model model, Principal principal) {
        PilotoDTO piloto = pilotoService.getPilotoById(idPiloto);
        if(piloto == null) {
            model.addAttribute("error", "No se ha encontrado el piloto con id " + idPiloto);
            return "redirect:/equipos";
        }
        if(usuarioService.hasEditPermissions(principal, piloto.getEquipo())) {
            model.addAttribute("puedeEditar", true);
        } else {
            model.addAttribute("puedeEditar", false);
        }
        model.addAttribute("titulo", "Piloto: " + piloto.getNombre());
        model.addAttribute("piloto", piloto);
        return "pilotos/seePiloto";
    }

    @GetMapping("/crearPiloto")
    public String crearPiloto(Principal principal, Model model) {
        if (principal == null) {
            return "redirect:/login";
        }
        UsuarioDTO usuario = usuarioService.getUsuarioByNombreUsuario(principal.getName());
        if(usuario.getEquipo() == null) {
            model.addAttribute("error", "El usuario " + principal.getName() + " no pertenece a ningún equipo");
            return "redirect:/equipos";
        }
        PilotoDTO piloto = new PilotoDTO();
        piloto.setEquipo(usuario.getEquipo());
        model.addAttribute("titulo", "Crear Piloto para equipo " + usuario.getEquipo().getNombre());
        model.addAttribute("piloto", piloto);
        return "pilotos/createPiloto";
    }

    @GetMapping("/editarPiloto/{idPiloto}")
    public String editarPiloto(@PathVariable("idPiloto") Long idPiloto, Model model) {
        PilotoDTO piloto = pilotoService.getPilotoById(idPiloto);
        model.addAttribute("titulo", "Editar Piloto: " + piloto.getNombre());
        model.addAttribute("piloto", piloto);
        return "pilotos/createPiloto";
    }

    @PostMapping("/guardarPiloto")
    public String guardarPiloto(@ModelAttribute("piloto") PilotoDTO piloto, Model model,
                                @RequestParam("file") MultipartFile foto, RedirectAttributes attributes) {
        if(foto != null && !foto.isEmpty()) {
            if (piloto.getId() != null && piloto.getId() > 0 && piloto.getFoto() != null
                    && !piloto.getFoto().isEmpty()) {
                uploadFileService.delete(piloto.getFoto(), Constants.PILOTOS);
            }
            String nombreImagen = null;
            try {
                nombreImagen = uploadFileService.copy(foto, Constants.PILOTOS);
            } catch (IOException e) {
                e.printStackTrace();
            }
            attributes.addFlashAttribute("success", "Has subido correctamente '" + nombreImagen + "'");
            piloto.setFoto(nombreImagen);
        }


        EquipoDTO equipo = equipoService.getEquipoById(piloto.getEquipo().getId());
        piloto.setEquipo(equipo);
        try {
            if(piloto.getId() != null && piloto.getId() > 0) {
                pilotoService.updatePiloto(piloto);
                attributes.addFlashAttribute("success", "Piloto actualizado correctamente");
            } else {
                pilotoService.addPiloto(piloto);
                attributes.addFlashAttribute("success", "Piloto creado correctamente");
            }
        } catch (IllegalArgumentException e) {
            attributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/pilotos/byEquipo/" + equipo.getId();
    }

    @GetMapping("/eliminarPiloto/{idPiloto}")
    public String eliminarPiloto(@PathVariable("idPiloto") Long idPiloto, Model model, RedirectAttributes attributes) {
        PilotoDTO piloto = pilotoService.getPilotoById(idPiloto);
        if(piloto != null) {
            if(piloto.getFoto() != null && !piloto.getFoto().isEmpty()) {
                uploadFileService.delete(piloto.getFoto(), Constants.PILOTOS);
            }
            pilotoService.deletePiloto(idPiloto);
            attributes.addFlashAttribute("success", "Piloto eliminado correctamente");
            return "redirect:/pilotos/byEquipo/" + piloto.getEquipo().getId();
        } else {
            attributes.addFlashAttribute("error", "No se ha podido eliminar el piloto");
            return "redirect:/pilotos/";
        }
    }

    @GetMapping("/verImagen/{filename}")
    public ResponseEntity<Resource> verImagen(@PathVariable String filename) {
        Resource recurso = null;
        try {
            recurso = uploadFileService.load(filename, Constants.PILOTOS);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + recurso.getFilename() + "\"")
                .body(recurso);
    }

    @GetMapping("/buscar")
    @ResponseBody
    public List<PilotoDTO> buscarPilotos(@RequestParam String campo, @RequestParam String valor) {
        return switch (campo.toLowerCase()) {
            case "nombre" -> pilotoService.getPilotosByNombre(valor);
            case "apellidos" -> pilotoService.getPilotosByApellido(valor);
            case "siglas" -> List.of(pilotoService.getPilotoBySiglas(valor));
            case "dorsal" -> List.of(pilotoService.getPilotoByDorsal(Integer.parseInt(valor)));
            case "pais" -> pilotoService.getPilotosByPais(valor);
            case "twitter" -> List.of(pilotoService.getPilotoByTwitter(valor));
            case "equipo" -> pilotoService.getPilotosByEquipoNombre(valor);
            default -> throw new IllegalArgumentException("Campo de búsqueda no válido");
        };
    }

}
