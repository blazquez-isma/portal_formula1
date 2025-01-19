package com.uah.ismael.portal_formula1.controller;

import com.uah.ismael.portal_formula1.dto.CircuitoDTO;
import com.uah.ismael.portal_formula1.dto.CocheDTO;
import com.uah.ismael.portal_formula1.dto.UsuarioDTO;
import com.uah.ismael.portal_formula1.service.CircuitoService;
import com.uah.ismael.portal_formula1.service.CocheService;
import com.uah.ismael.portal_formula1.service.SimulacionService;
import com.uah.ismael.portal_formula1.service.UsuarioService;
import com.uah.ismael.portal_formula1.utils.EstiloConduccion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
public class SimulacionController {

    @Autowired
    private SimulacionService simulacionService;

    @Autowired
    private CocheService cocheService;

    @Autowired
    private CircuitoService circuitoService;

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/simulaciones/combustible")
    public String mostrarSimulacionCombustible(Model model, Principal principal, RedirectAttributes redirectAttributes) {
        try {
            validarUsuario(principal, model);
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/equipos";
        }
        return "simulaciones/combustible";
    }

    @PostMapping("/simulaciones/combustible")
    public String calcularCombustible(
            @RequestParam("cocheId") Long cocheId,
            @RequestParam("circuitoId") Long circuitoId,
            Model model,
            Principal principal,
            RedirectAttributes redirectAttributes) {

        try {
            validarUsuario(principal, model);
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/equipos";
        }

        CocheDTO coche = cocheService.getCocheById(cocheId);
        CircuitoDTO circuito = circuitoService.getCircuitoById(circuitoId);

        double consumoPorVuelta = simulacionService.calcularConsumoPorVuelta(coche, circuito);
        double consumoTotal = simulacionService.calcularConsumoTotal(coche, circuito, circuito.getNumeroVueltas());

        model.addAttribute("cocheSeleccionado", coche);
        model.addAttribute("circuitoSeleccionado", circuito);
        model.addAttribute("consumoPorVuelta", consumoPorVuelta);
        model.addAttribute("consumoTotal", consumoTotal);

        return "simulaciones/combustible";
    }


    @GetMapping("/simulaciones/ers")
    public String mostrarSimulacionERS(Model model, Principal principal, RedirectAttributes redirectAttributes) {
        try {
            validarUsuario(principal, model);
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/equipos";
        }
        model.addAttribute("estilosConduccion", EstiloConduccion.values());
        return "simulaciones/ers";
    }

    @PostMapping("/simulaciones/ers")
    public String calcularERS(
            @RequestParam("cocheId") Long cocheId,
            @RequestParam("circuitoId") Long circuitoId,
            @RequestParam("estiloConduccion") String estiloConduccion,
            Model model,
            Principal principal,
            RedirectAttributes redirectAttributes) {

        try {
            validarUsuario(principal, model);
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/equipos";
        }

        CocheDTO coche = cocheService.getCocheById(cocheId);
        CircuitoDTO circuito = circuitoService.getCircuitoById(circuitoId);

        double ersPorVuelta = simulacionService.calcularERSPorVuelta(coche, circuito, estiloConduccion);
        int vueltasParaCargar = simulacionService.calcularVueltasParaCargarBateria(coche, circuito, estiloConduccion);

        model.addAttribute("cocheSeleccionado", coche);
        model.addAttribute("circuitoSeleccionado", circuito);
        model.addAttribute("estiloSeleccionado", estiloConduccion);
        model.addAttribute("estilosConduccion", EstiloConduccion.values());
        model.addAttribute("ersPorVuelta", ersPorVuelta);
        model.addAttribute("vueltasParaCargar", vueltasParaCargar);

        return "simulaciones/ers";
    }


    private void validarUsuario(Principal principal, Model model) {
        if (principal == null) {
            throw new RuntimeException("Debe iniciar sesión para acceder a esta función.");
        }
        UsuarioDTO usuario = usuarioService.getUsuarioByNombreUsuario(principal.getName());
        if (usuario == null || usuario.getEquipo() == null) {
            throw new RuntimeException("Debe pertenecer a un equipo para acceder a esta función.");
        }
        model.addAttribute("titulo", "Simulación para " + usuario.getEquipo().getNombre());
        model.addAttribute("coches", cocheService.getCochesByEquipoId(usuario.getEquipo().getId()));
        model.addAttribute("circuitos", circuitoService.getAllCircuitos());
    }
}
