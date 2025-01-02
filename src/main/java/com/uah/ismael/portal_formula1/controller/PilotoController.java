package com.uah.ismael.portal_formula1.controller;

import com.uah.ismael.portal_formula1.dto.EquipoDTO;
import com.uah.ismael.portal_formula1.dto.PilotoDTO;
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

    @GetMapping
    public String verPilotos(@RequestParam(defaultValue = "0") int page,
                             @RequestParam(defaultValue = "5") int size,
                             @RequestParam(defaultValue = "nombre") String sortField,
                             @RequestParam(defaultValue = "asc") String sortDir,
                             Model model) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortDir), sortField));
        Page<PilotoDTO> pilotosPage = pilotoService.getAllPilotos(pageable);

        model.addAttribute("titulo", "Pilotos");
        PageUtil.addPaginationAttributes(model, pilotosPage, page, sortField, sortDir);
        return "pilotos/listPilotos";
    }

    @GetMapping("/{idEquipo}")
    public String verPilotosDeEquipo(@PathVariable("idEquipo") Long idEquipo,
                                     @RequestParam(defaultValue = "0") int page,
                                     @RequestParam(defaultValue = "5") int size,
                                     @RequestParam(defaultValue = "nombre") String sortField,
                                     @RequestParam(defaultValue = "asc") String sortDir,
                                     Model model) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortDir), sortField));
        Page<PilotoDTO> pilotosPage = pilotoService.getPilotosByEquipoId(pageable, idEquipo);

        EquipoDTO equipo = equipoService.getEquipoById(idEquipo);
        model.addAttribute("titulo", "Pilotos de " + equipo.getNombre());
        PageUtil.addPaginationAttributes(model, pilotosPage, page, sortField, sortDir);
        return "pilotos/listPilotos";
    }

    @GetMapping("/verPiloto/{idPiloto}")
    public String verPiloto(@PathVariable("idPiloto") Long idPiloto, Model model) {
        PilotoDTO piloto = pilotoService.getPilotoById(idPiloto);
        model.addAttribute("titulo", "Piloto: " + piloto.getNombre());
        model.addAttribute("piloto", piloto);
        return "pilotos/seePiloto";
    }

    @GetMapping("/crearPiloto/{idEquipo}")
    public String crearPiloto(@PathVariable("idEquipo") Long idEquipo, Model model) {
        EquipoDTO equipo = equipoService.getEquipoById(idEquipo);
        PilotoDTO piloto = new PilotoDTO();
        piloto.setEquipo(equipo);
        model.addAttribute("titulo", "Crear Piloto");
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
    public String guardarPiloto(@ModelAttribute("piloto") PilotoDTO piloto, Model model, @RequestParam("idEquipo") Long idEquipo,
                                @RequestParam("file") MultipartFile foto, RedirectAttributes attributes) {

        if(foto != null && !foto.isEmpty()) {
            if (piloto.getId() != null && piloto.getId() > 0 && piloto.getFoto() != null
                    && !piloto.getFoto().isEmpty()) {
                uploadFileService.delete(piloto.getFoto());
            }
            String nombreImagen = null;
            try {
                nombreImagen = uploadFileService.copy(foto);
            } catch (IOException e) {
                e.printStackTrace();
            }
            attributes.addFlashAttribute("success", "Has subido correctamente '" + nombreImagen + "'");
            piloto.setFoto(nombreImagen);
        }

        if(piloto.getId() != null && piloto.getId() > 0) {
            pilotoService.updatePiloto(piloto);
            attributes.addFlashAttribute("success", "Piloto actualizado correctamente");
        } else {
//            EquipoDTO equipo = equipoService.addPilotoToEquipo(piloto);
            EquipoDTO equipo = equipoService.getEquipoById(idEquipo);
            piloto.setEquipo(equipo);
            pilotoService.addPiloto(piloto);
            attributes.addFlashAttribute("success", "Piloto creado correctamente");
        }

        return "redirect:/pilotos/" + piloto.getEquipo().getId();
    }

    @GetMapping("/eliminarPiloto/{idPiloto}")
    public String eliminarPiloto(@PathVariable("idPiloto") Long idPiloto, Model model, RedirectAttributes attributes) {
        PilotoDTO piloto = pilotoService.getPilotoById(idPiloto);
        if(piloto != null) {
            if(piloto.getFoto() != null && !piloto.getFoto().isEmpty()) {
                uploadFileService.delete(piloto.getFoto());
            }
            pilotoService.deletePiloto(idPiloto);
            attributes.addFlashAttribute("success", "Piloto eliminado correctamente");
            return "redirect:/pilotos/" + piloto.getEquipo().getId();
        } else {
            attributes.addFlashAttribute("error", "No se ha podido eliminar el piloto");
            return "redirect:/pilotos/";
        }
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
