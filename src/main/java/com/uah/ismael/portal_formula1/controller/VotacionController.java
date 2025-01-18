package com.uah.ismael.portal_formula1.controller;

import com.uah.ismael.portal_formula1.dto.PilotoDTO;
import com.uah.ismael.portal_formula1.dto.VotacionDTO;
import com.uah.ismael.portal_formula1.dto.VotoDTO;
import com.uah.ismael.portal_formula1.service.PilotoService;
import com.uah.ismael.portal_formula1.service.UploadFileService;
import com.uah.ismael.portal_formula1.service.VotacionService;
import com.uah.ismael.portal_formula1.service.VotoService;
import com.uah.ismael.portal_formula1.utils.Constants;
import com.uah.ismael.portal_formula1.utils.PageUtil;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
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
        return "votaciones/listAllVotaciones";
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
        return "votaciones/listVotacionesActivas";
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
        return "votaciones/listVotacionesFinalizadas";
    }

    @GetMapping("/verVotacion/{idVotacion}")
    public String verVotacion(@PathVariable("idVotacion") Long idVotacion,
                              @RequestParam(defaultValue = "nombre") String sortField,
                              @RequestParam(defaultValue = "asc") String sortDir,
                              Model model) {
        VotacionDTO votacion = votacionService.getVotacionById(idVotacion);
        boolean isActiva = votacion.getFechaLimite().after(new java.util.Date());
        List<VotoDTO> votos = votoService.getVotosByVotacionId(idVotacion);

        // Calcular el porcentaje de votos de cada piloto
        int totalVotos = votos.size();
        votacion.getPilotos().forEach(piloto -> {
            long votosPiloto = votos.stream().filter(voto -> voto.getPiloto().getId().equals(piloto.getId())).count();
            double porcentajeVotos = totalVotos > 0 ? (votosPiloto * 100.0) / totalVotos : 0;
            piloto.setPorcentajeVotos(porcentajeVotos);
        });

        // Ordenar la lista de pilotos
        Comparator<PilotoDTO> comparator = PilotoDTO.getPilotoPageableComparator(PageRequest.of(0, 10, Sort.by(Sort.Direction.fromString(sortDir), sortField)));
        votacion.getPilotos().sort(comparator);

        double maxPorcentajeVotos = votacion.getPilotos().stream()
                .max(Comparator.comparingDouble(PilotoDTO::getPorcentajeVotos))
                .orElseThrow(() -> new IllegalArgumentException("No pilotos found"))
                .getPorcentajeVotos();
        model.addAttribute("maxPorcentajeVotos", maxPorcentajeVotos);

        model.addAttribute("titulo", "Votación: " + votacion.getTitulo());
        model.addAttribute("votacion", votacion);
        model.addAttribute("isActiva", isActiva);
        model.addAttribute("votos", votos);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
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
                                  Model model,
                                  RedirectAttributes redirectAttributes) {
        if (!fechaLimiteStr.matches("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}")) {
            model.addAttribute("error", "El formato de fecha y hora no es válido");
            model.addAttribute("votacion", votacion);
            return "votaciones/createVotacion";
        }

        // Convertir fechaLimite de String a Timestamp
        Timestamp fechaLimite = Timestamp.valueOf(fechaLimiteStr.replace("T", " ") + ":00");
        votacion.setFechaLimite(fechaLimite);

        if (idsPilotos.size() < 5 || idsPilotos.size() > 10) {
            model.addAttribute("error", "Debes seleccionar entre 5 y 10 pilotos");
            model.addAttribute("votacion", votacion);
            return "votaciones/createVotacion";
        }

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
        model.addAttribute("voto", new VotoDTO());
        return "votaciones/formVotar";
    }

    @PostMapping("/votar")
    public String votar(@RequestParam("votacionId") Long votacionId,
                        @RequestParam("pilotoId") Optional<Long> pilotoIdOpt,
                        @ModelAttribute("voto") VotoDTO voto,
                        RedirectAttributes redirectAttributes) {

        if (pilotoIdOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Debes seleccionar un piloto");
            return "redirect:/votaciones/votar/" + votacionId;
        }
        Long pilotoId = pilotoIdOpt.get();

        System.out.println("Votacion: " + votacionId + " Piloto: " + pilotoId + "\n Voto: " + voto);

        if (voto.getNombreVotante() == null || voto.getNombreVotante().isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "El nombre del votante no puede estar vacío");
            return "redirect:/votaciones/votar/" + votacionId;
        }

        if (voto.getEmail() == null || voto.getEmail().isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "El email del votante no puede estar vacío");
            return "redirect:/votaciones/votar/" + votacionId;
        }

        try {
            VotacionDTO votacion = votacionService.getVotacionById(votacionId);
            voto.setVotacion(votacion);
            PilotoDTO piloto = pilotoService.getPilotoById(pilotoId);
            voto.setPiloto(piloto);
            votoService.addVoto(voto);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/votaciones/votar/" + voto.getVotacion().getId();
        }
        redirectAttributes.addFlashAttribute("success", "Voto realizado correctamente");
        return "redirect:/votaciones/verVotacion/" + voto.getVotacion().getId();
    }

}
