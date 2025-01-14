package com.uah.ismael.portal_formula1.controller;

import com.uah.ismael.portal_formula1.dto.CircuitoDTO;
import com.uah.ismael.portal_formula1.service.CircuitoService;
import com.uah.ismael.portal_formula1.service.UploadFileService;
import com.uah.ismael.portal_formula1.utils.Constants;
import com.uah.ismael.portal_formula1.utils.PageUtil;
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
@RequestMapping("/circuitos")
public class CircuitoController {

    Logger logger = LoggerFactory.getLogger(CircuitoController.class);

    @Autowired
    private CircuitoService circuitoService;

    @Autowired
    private UploadFileService uploadFileService;

    @GetMapping
    public String verCircuitos(@RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = Constants.DEFAULT_SIZE) int size,
                               @RequestParam(defaultValue = "titulo") String sortField,
                               @RequestParam(defaultValue = "asc") String sortDir,
                               Model model) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortDir), sortField));
        Page<CircuitoDTO> circuitosPage = circuitoService.getAllCircuitos(pageable);
        model.addAttribute("titulo", "Circuitos");
        PageUtil.addPaginationAttributes(model,circuitosPage, page, sortField, sortDir);
        return "circuitos/listCircuitos";
    }

    @GetMapping("/calendario")
    public String verCalendario(Model model) {
        List<CircuitoDTO> circuitos = circuitoService.getCircuitosFechaNotNull();
        model.addAttribute("titulo", "Calendario");
        return "circuitos/calendario";
    }
    
    @GetMapping("/verCircuito/{idCircuito}")
    public String verCircuito(@PathVariable("idCircuito") Long idCircuito, Model model, Principal principal) {
        CircuitoDTO circuito = circuitoService.getCircuitoById(idCircuito);
        if(circuito == null) {
            return "redirect:/circuitos";
        }
        model.addAttribute("titulo", "Circuito: " + circuito.getNombre());
        model.addAttribute("circuito", circuito);
        return "circuitos/seeCircuito";
    }

    @GetMapping("/crearCircuito")
    public String crearCircuito(Model model) {
        model.addAttribute("titulo", "Crear Circuito");
        model.addAttribute("circuito", new CircuitoDTO());
        return "circuitos/createCircuito";
    }

    @GetMapping("/editarCircuito/{idCircuito}")
    public String editarCircuito(@PathVariable("idCircuito") Long idCircuito, Model model) {
        CircuitoDTO circuito = circuitoService.getCircuitoById(idCircuito);
        if(circuito == null) {
            return "redirect:/circuitos";
        }
        model.addAttribute("titulo", "Editar Circuito: " + circuito.getNombre());
        model.addAttribute("circuito", circuito);
        return "circuitos/createEquipo";
    }

    @PostMapping("/guardarCircuito")
    public String guardarCircuito(@ModelAttribute("circuito") CircuitoDTO circuito,
                                  @RequestParam("file") MultipartFile trazado, RedirectAttributes attributes){
        if(trazado != null && !trazado.isEmpty()) {
            if(circuito.getId() != null && circuito.getId() > 0
                    && circuito.getTrazado() != null && !circuito.getTrazado().isEmpty()) {
                uploadFileService.delete(circuito.getTrazado(), Constants.CIRCUITOS);
            }
            String nombreImagen = null;
            try {
                nombreImagen = uploadFileService.copy(trazado, Constants.CIRCUITOS);
            } catch (IOException e) {
                e.printStackTrace();
            }
            attributes.addFlashAttribute("success", "Has subido correctamente '" + nombreImagen + "'");
            circuito.setTrazado(nombreImagen);
        }

        try {
            if(circuito.getId() != null && circuito.getId() > 0) {
               if(circuitoService.updateCircuito(circuito)) {
                   attributes.addFlashAttribute("success", "Circuito actualizado correctamente");
               } else {
                   attributes.addFlashAttribute("error", "No se ha podido actualizar el circuito");
               }
            } else {
                circuitoService.addCircuito(circuito);
                attributes.addFlashAttribute("success", "Circuito creado correctamente");
            }

        } catch (IllegalArgumentException e) {
            attributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/circuitos";
    }

    @GetMapping("/borrarCircuito/{idCircuito}")
    public String borrarCircuito(@PathVariable("idCircuito") Long idCircuito, RedirectAttributes attributes) {
        CircuitoDTO circuito = circuitoService.getCircuitoById(idCircuito);
        if(circuito != null) {
            if(circuito.getTrazado() != null && !circuito.getTrazado().isEmpty()) {
                uploadFileService.delete(circuito.getTrazado(), Constants.CIRCUITOS);
            }
            circuitoService.deleteCircuito(idCircuito);
            attributes.addFlashAttribute("success", "Circuito eliminado correctamente");
        } else {
            attributes.addFlashAttribute("error", "No se ha podido eliminar el circuito");
        }
        return "redirect:/circuitos";
    }


    @GetMapping("/verImagen/{filename}")
    public ResponseEntity<Resource> verImagen(@PathVariable String filename) {
        Resource recurso = null;
        try {
            recurso = uploadFileService.load(filename, Constants.CIRCUITOS);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + recurso.getFilename() + "\"")
                .body(recurso);
    }
}
