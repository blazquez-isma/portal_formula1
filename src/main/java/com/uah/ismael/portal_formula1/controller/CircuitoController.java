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
import java.sql.Date;
import java.util.*;

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
        Page<CircuitoDTO> circuitosPage = circuitoService.getAllCircuitosPage(pageable);
        model.addAttribute("titulo", "Circuitos");
        PageUtil.addPaginationAttributes(model,circuitosPage, page, sortField, sortDir);
        return "circuitos/listCircuitos";
    }

    @GetMapping("/verCalendario")
    public String verCalendario(@RequestParam(value = "mes", required = false) Integer mes,
                                Model model) {
        List<CircuitoDTO> circuitos = circuitoService.getCircuitosFechaNotNull();
        List<CircuitoDTO> circuitosPorMes = null;
        List<Integer> auxIntMeses = Arrays.asList(Calendar.JANUARY, Calendar.FEBRUARY, Calendar.MARCH, Calendar.APRIL,
                Calendar.MAY, Calendar.JUNE, Calendar.JULY, Calendar.AUGUST, Calendar.SEPTEMBER, Calendar.OCTOBER,
                Calendar.NOVEMBER, Calendar.DECEMBER);
        List<String> nombresMeses = Arrays.asList(
                "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
                "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"
        );
        if (mes != null && auxIntMeses.contains(mes)) {
            circuitosPorMes = circuitos.stream().filter(c -> {
                Calendar cal = Calendar.getInstance();
                cal.setTime(c.getFechaCalendario());
                return cal.get(Calendar.MONTH) == mes;
            }).toList();
        }

        model.addAttribute("titulo", "Calendario de Circuitos");
        model.addAttribute("circuitosPorMes", circuitosPorMes);
        model.addAttribute("mesSeleccionado", mes);
        model.addAttribute("nombresMeses", nombresMeses);
        return "circuitos/showCalendar";
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
        model.addAttribute("addCalendario", false);
        model.addAttribute("editMode", false);
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
        model.addAttribute("addCalendario", false);
        model.addAttribute("editMode", true);
        return "circuitos/createCircuito";
    }

    @GetMapping("/anadirACalendario/{idCircuito}")
    public String anadirACalendario(@PathVariable("idCircuito") Long idCircuito, Model model) {
        CircuitoDTO circuito = circuitoService.getCircuitoById(idCircuito);
        if(circuito == null) {
            return "redirect:/circuitos";
        }
        model.addAttribute("titulo", "Añadir a calendario: " + circuito.getNombre());
        model.addAttribute("circuito", circuito);
        model.addAttribute("addCalendario", true);
        model.addAttribute("editMode", true);
        return "circuitos/createCircuito";
    }

    @GetMapping("/quitarDeCalendario/{idCircuito}")
    public String quitarDeCalendario(@PathVariable("idCircuito") Long idCircuito, RedirectAttributes attributes) {
        CircuitoDTO circuito = circuitoService.getCircuitoById(idCircuito);
        if(circuito != null) {
            circuito.setFechaCalendario(null);
            circuitoService.updateCircuito(circuito);
            attributes.addFlashAttribute("success", "Circuito eliminado del calendario correctamente");
        } else {
            attributes.addFlashAttribute("error", "No se ha podido eliminar el circuito del calendario");
        }
        return "redirect:/circuitos";
    }

    @PostMapping("/guardarCircuitoCalendario")
    public String guardarCircuitoCalendario(@ModelAttribute("circuito") CircuitoDTO circuito,
                                            @RequestParam("fechaCalendarioStr") String fechaCalendarioStr,
                                            RedirectAttributes attributes) {
        try {
            circuito = circuitoService.getCircuitoById(circuito.getId());
            Date fechaCalendario = (fechaCalendarioStr == null || fechaCalendarioStr.isEmpty()) ? null : Date.valueOf(fechaCalendarioStr);
            circuito.setFechaCalendario(fechaCalendario);

            if(circuito.getId() != null && circuito.getId() > 0) {
                if(circuitoService.updateCircuito(circuito)) {
                    attributes.addFlashAttribute("success", "Circuito añadido al calendario correctamente");
                } else {
                    attributes.addFlashAttribute("error", "No se ha podido añadir el circuito al calendario");
                }
            }
        } catch (IllegalArgumentException e) {
            attributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/circuitos";
    }

    @PostMapping("/guardarCircuito")
    public String guardarCircuito(@ModelAttribute("circuito") CircuitoDTO circuito,
                                  @RequestParam("file") MultipartFile trazado,
                                  @RequestParam("fechaCalendarioStr") String fechaCalendarioStr,
                                  RedirectAttributes attributes){
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
            Date fechaCalendario = (fechaCalendarioStr == null || fechaCalendarioStr.isEmpty()) ? null : Date.valueOf(fechaCalendarioStr);
            circuito.setFechaCalendario(fechaCalendario);

            if(circuito.getId() != null && circuito.getId() > 0) {
               if(circuitoService.updateCircuito(circuito)) {
                   attributes.addFlashAttribute("success", "Circuito actualizado correctamente");
               } else {
                   attributes.addFlashAttribute("error", "No se ha podido actualizar el circuito");
               }
            } else {
                if(trazado == null || trazado.isEmpty()) {
                    attributes.addFlashAttribute("error", "Debes subir un trazado para el circuito");
                }


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
            if(circuito.getFechaCalendario() != null){
                attributes.addFlashAttribute("error", "No se puede eliminar un circuito que está en el calendario");
                return "redirect:/circuitos";
            }
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
