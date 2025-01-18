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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
@RequestMapping("/simulaciones")
public class SimulacionController {

    @Autowired
    private SimulacionService simulacionService;

    @Autowired
    private CocheService cocheService;

    @Autowired
    private CircuitoService circuitoService;

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public String mostrarSimulacion(Model model, Principal principal, RedirectAttributes redirectAttributes) {
        if (principal == null) {
            return "redirect:/login";
        }
        UsuarioDTO usuario = usuarioService.getUsuarioByNombreUsuario(principal.getName());
        if (usuario == null) {
            return "redirect:/login";
        }
        if(usuario.getEquipo() == null) {
            redirectAttributes.addFlashAttribute("error", "Debes estar en un equipo para acceder a esta sección");
            return "redirect:/equipos";
        }
        model.addAttribute("titulo", "Simulación para " + usuario.getEquipo().getNombre());
        model.addAttribute("coches", cocheService.getCochesByEquipoId(usuario.getEquipo().getId()));
        model.addAttribute("circuitos", circuitoService.getAllCircuitos());
        model.addAttribute("estilosConduccion", EstiloConduccion.values());
        return "simulaciones/simulacion";
    }

    @PostMapping("/combustible")
    public String calcularCombustible(
            @RequestParam("cocheId") Long cocheId,
            @RequestParam("circuitoId") Long circuitoId,
            Model model) {

        CocheDTO coche = cocheService.getCocheById(cocheId);
        CircuitoDTO circuito = circuitoService.getCircuitoById(circuitoId);

        double consumoPorVuelta = simulacionService.calcularConsumoPorVuelta(coche, circuito);
        double consumoTotal = simulacionService.calcularConsumoTotal(coche, circuito, circuito.getNumeroVueltas());

        model.addAttribute("titulo", "Simulación de combustible");
        model.addAttribute("coche", coche);
        model.addAttribute("circuito", circuito);
        model.addAttribute("consumoPorVuelta", consumoPorVuelta);
        model.addAttribute("consumoTotal", consumoTotal);

        return "simulaciones/resultCombustible";
    }

    @PostMapping("/ers")
    public String calcularERS(
            @RequestParam("cocheId") Long cocheId,
            @RequestParam("circuitoId") Long circuitoId,
            @RequestParam("estiloConduccion") String estiloConduccion,
            Model model) {

        CocheDTO coche = cocheService.getCocheById(cocheId);
        CircuitoDTO circuito = circuitoService.getCircuitoById(circuitoId);

        double ersPorVuelta = simulacionService.calcularERSPorVuelta(coche, circuito, estiloConduccion);
        int vueltasParaCargar = simulacionService.calcularVueltasParaCargarBateria(coche, circuito, estiloConduccion);

        model.addAttribute("titulo", "Simulación de ERS");
        model.addAttribute("coche", coche);
        model.addAttribute("circuito", circuito);
        model.addAttribute("ersPorVuelta", ersPorVuelta);
        model.addAttribute("vueltasParaCargar", vueltasParaCargar);
        model.addAttribute("estiloConduccion", estiloConduccion);

        return "simulaciones/resultERS";
    }
}