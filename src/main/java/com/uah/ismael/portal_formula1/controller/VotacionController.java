package com.uah.ismael.portal_formula1.controller;

import com.uah.ismael.portal_formula1.dto.PilotoDTO;
import com.uah.ismael.portal_formula1.dto.VotacionDTO;
import com.uah.ismael.portal_formula1.dto.VotoDTO;
import com.uah.ismael.portal_formula1.model.repository.VotoRepository;
import com.uah.ismael.portal_formula1.service.PilotoService;
import com.uah.ismael.portal_formula1.service.UploadFileService;
import com.uah.ismael.portal_formula1.service.VotacionService;
import com.uah.ismael.portal_formula1.service.VotoService;
import com.uah.ismael.portal_formula1.utils.Constants;
import com.uah.ismael.portal_formula1.utils.PageUtil;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/votaciones")
public class VotacionController {

    Logger log = org.slf4j.LoggerFactory.getLogger(VotacionController.class);

    @Autowired
    private VotacionService votacionService;

    @Autowired
    private VotoService votoService;

    @Autowired
    private PilotoService pilotoService;

    @Autowired
    private UploadFileService uploadFileService;


    @GetMapping
    public String verTodasVotaciones(@RequestParam(defaultValue = "0") int page,
                                     @RequestParam(defaultValue = Constants.DEFAULT_SIZE) int size,
                                     @RequestParam(defaultValue = "titulo") String sortField,
                                     @RequestParam(defaultValue = "asc") String sortDir,
                                     Model model) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortDir), sortField));
        Page<VotacionDTO> votacionPage = votacionService.getAllVotaciones(pageable);
        model.addAttribute("titulo", "Listado de Votaciones");
        PageUtil.addPaginationAttributes(model, votacionPage, page, sortField, sortDir);
        return "votaciones/listVotaciones";
    }

    @GetMapping("/activas")
    public String verVotacionesActivas(@RequestParam(defaultValue = "0") int page,
                                       @RequestParam(defaultValue = Constants.DEFAULT_SIZE) int size,
                                       @RequestParam(defaultValue = "titulo") String sortField,
                                       @RequestParam(defaultValue = "asc") String sortDir,
                                       Model model) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortDir), sortField));
        Page<VotacionDTO> votacionPage = votacionService.getVotacionesActivas(pageable);
        model.addAttribute("titulo", "Listado de Votaciones Activas");
        PageUtil.addPaginationAttributes(model, votacionPage, page, sortField, sortDir);
        return "votaciones/listVotaciones";
    }

    @GetMapping("/finalizadas")
    public String verVotacionesFinalizadas(@RequestParam(defaultValue = "0") int page,
                                          @RequestParam(defaultValue = Constants.DEFAULT_SIZE) int size,
                                          @RequestParam(defaultValue = "titulo") String sortField,
                                          @RequestParam(defaultValue = "asc") String sortDir,
                                          Model model) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortDir), sortField));
        Page<VotacionDTO> votacionPage = votacionService.getVotacionesFinalizadas(pageable);
        model.addAttribute("titulo", "Listado de Votaciones Finalizadas");
        PageUtil.addPaginationAttributes(model, votacionPage, page, sortField, sortDir);
        return "votaciones/listVotaciones";
    }

    @GetMapping("/verVotación/{idVotacion}")
    public String verVotacion(@PathVariable("idVotacion") Long idVotacion,
                            Model model) {
        VotacionDTO votacion = votacionService.getVotacionById(idVotacion);
        boolean isActiva = votacion.getFechaLimite().after(new java.util.Date());
        List<VotoDTO> votos = votoService.getVotosByVotacionId(idVotacion);

        model.addAttribute("titulo", "Votación: " + votacion.getTitulo());
        model.addAttribute("votacion", votacion);
        model.addAttribute("isActiva", isActiva);
        model.addAttribute("votos", votos);
        return "votaciones/seeVotacion";
    }

    @GetMapping("/crearVotacion")
    @PreAuthorize("hasRole('ROLE_ADMINISTRADOR')")
    public String crearVotacion(Model model) {
        VotacionDTO votacion = new VotacionDTO();
        votacion.setPilotos(new ArrayList<>());
        model.addAttribute("votacion", votacion);
        return "votaciones/createVotacion";
    }

//    @GetMapping("/editarVotacion/{idVotacion}")
//    @PreAuthorize("hasRole('ROLE_ADMINISTRADOR')")
//    public String editarVotacion(@PathVariable("idVotacion") Long idVotacion,
//                               Model model) {
//        VotacionDTO votacion = votacionService.getVotacionById(idVotacion);
//        model.addAttribute("votacion", votacion);
//        return "votaciones/createVotacion";
//    }

    @PostMapping("/guardarVotacion")
    @PreAuthorize("hasRole('ROLE_ADMINISTRADOR')")
    public String guardarVotacion(@ModelAttribute("votacion") VotacionDTO votacion,
                                  @RequestParam("fechaLimiteStr") String fechaLimiteStr,
                                  @RequestParam("idsPilotos") List<Long> idsPilotos,
                                  RedirectAttributes redirectAttributes) {
        System.out.println("Fecha Limite: " + fechaLimiteStr);

        if (!fechaLimiteStr.matches("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}")) {
            redirectAttributes.addFlashAttribute("error", "El formato de fecha y hora no es válido");
            return "redirect:/votaciones/crearVotacion";
        }
        // Convertir fechaLimite de String a Timestamp
        Timestamp fechaLimite = Timestamp.valueOf(fechaLimiteStr.replace("T", " ") + ":00");
        votacion.setFechaLimite(fechaLimite);

        List<PilotoDTO> pilotos = idsPilotos.stream()
                .map(pilotoService::getPilotoById)
                .collect(Collectors.toList());
        votacion.setPilotos(pilotos);

        if (votacion.getId() == null) {
            votacionService.addVotacion(votacion);
            redirectAttributes.addFlashAttribute("success", "Votación creada correctamente");
        } else {
            votacionService.updateVotacion(votacion);
            redirectAttributes.addFlashAttribute("success", "Votación actualizada correctamente");
        }
        return "redirect:/votaciones";
    }

    @GetMapping("/eliminarVotacion/{idVotacion}")
    @PreAuthorize("hasRole('ROLE_ADMINISTRADOR')")
    public String eliminarVotacion(@PathVariable("idVotacion") Long idVotacion, RedirectAttributes redirectAttributes) {
        VotacionDTO votacion = votacionService.getVotacionById(idVotacion);
        if(votacion != null) {
            votacionService.deleteVotacion(idVotacion);
            redirectAttributes.addFlashAttribute("success", "Votación eliminada correctamente");
        } else {
            redirectAttributes.addFlashAttribute("error", "No se ha encontrado la votación con id " + idVotacion);
        }
        return "redirect:/votaciones";
    }

    @GetMapping("/votar/{idVotacion}")
    public String votar(@PathVariable("idVotacion") Long idVotacion, Model model) {
        VotacionDTO votacion = votacionService.getVotacionById(idVotacion);
        model.addAttribute("votacion", votacion);
        return "votaciones/formVotar";
    }

    @PostMapping("/votar")
    public String votar(@ModelAttribute("voto") VotoDTO voto, RedirectAttributes redirectAttributes) {
        try {
            votoService.addVoto(voto);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/votaciones/votar/" + voto.getVotacion().getId();
        }
        redirectAttributes.addFlashAttribute("success", "Voto realizado correctamente");
        return "redirect:/votaciones";
    }

}
