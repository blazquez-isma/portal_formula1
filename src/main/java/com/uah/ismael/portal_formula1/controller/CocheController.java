package com.uah.ismael.portal_formula1.controller;

import com.uah.ismael.portal_formula1.dto.CocheDTO;
import com.uah.ismael.portal_formula1.dto.EquipoDTO;
import com.uah.ismael.portal_formula1.paginator.PageUtil;
import com.uah.ismael.portal_formula1.service.CocheService;
import com.uah.ismael.portal_formula1.service.EquipoService;
import com.uah.ismael.portal_formula1.service.PilotoService;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/coches")
public class CocheController {

    Logger logger = LoggerFactory.getLogger(CocheController.class);

    @Autowired
    private EquipoService equipoService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private CocheService cocheService;

    @Autowired
    private PilotoService pilotoService;

    @RequestMapping("/{idEquipo}")
    public String verCochesDeEquipo(@PathVariable("idEquipo") Long idEquipo,
                                    @RequestParam(defaultValue = "0") int page,
                                    @RequestParam(defaultValue = "5") int size,
                                    @RequestParam(defaultValue = "nombre") String sortField,
                                    @RequestParam(defaultValue = "asc") String sortDir,
                                    Model model) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortDir), sortField));
        Page<CocheDTO> cochesPage = cocheService.getCochesByEquipoId(idEquipo, pageable);

        EquipoDTO equipo = equipoService.getEquipoById(idEquipo);
        model.addAttribute("titulo", "Coches del equipo " + equipo.getNombre());
        PageUtil.addPaginationAttributes(model, cochesPage, page, sortField, sortDir);
        return "coches/listCoches";
    }

    @GetMapping("/verCoche/{idCoche}")
    public String verCoche(@PathVariable("idCoche") Long idCoche, Model model) {
        CocheDTO coche = cocheService.getCocheById(idCoche);
        model.addAttribute("titulo", "Coche " + coche.getNombre());
        model.addAttribute("coche", coche);
        return "coches/seeCoche";
    }

    @GetMapping("/crearCoche/{idEquipo}")
    public String crearCoche(@PathVariable("idEquipo") Long idEquipo, Model model) {
        EquipoDTO equipo = equipoService.getEquipoById(idEquipo);
        CocheDTO coche = new CocheDTO();
        coche.setEquipo(equipo);
        model.addAttribute("titulo", "Crear coche para el equipo " + equipo.getNombre());
        model.addAttribute("coche", coche);
        return "coches/createCoche";
    }

    @GetMapping("/editarCoche/{idCoche}")
    public String editarCoche(@PathVariable("idCoche") Long idCoche, Model model) {
        CocheDTO coche = cocheService.getCocheById(idCoche);
        model.addAttribute("titulo", "Editar coche " + coche.getNombre());
        model.addAttribute("coche", coche);
        return "coches/createCoche";
    }

    @PostMapping("/guardarCoche")
    public String guardarCoche(@ModelAttribute CocheDTO coche, @RequestParam("idEquipo") Long idEquipo,
                               Model model, RedirectAttributes attributes) {
        if(coche.getId() == null) {
            EquipoDTO equipo = equipoService.getEquipoById(idEquipo);
            coche.setEquipo(equipo);
            cocheService.addCoche(coche);
            attributes.addFlashAttribute("success", "Coche creado correctamente");
        } else {
            cocheService.updateCoche(coche);
            attributes.addFlashAttribute("success", "Coche actualizado correctamente");
        }
        return "redirect:/coches/" + idEquipo;
    }

    @GetMapping("/eliminarCoche/{idCoche}")
    public String eliminarCoche(@PathVariable("idCoche") Long idCoche, RedirectAttributes attributes) {
        CocheDTO coche = cocheService.getCocheById(idCoche);
        if(coche != null) {
            cocheService.deleteCoche(idCoche);
            attributes.addFlashAttribute("success", "Coche eliminado correctamente");
            return "redirect:/coches/" + coche.getEquipo().getId();
        } else {
            attributes.addFlashAttribute("error", "No se ha podido eliminar el coche");
            return "redirect:/coches/";
        }
    }

}
