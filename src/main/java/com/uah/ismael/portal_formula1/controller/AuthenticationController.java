package com.uah.ismael.portal_formula1.controller;

import com.uah.ismael.portal_formula1.dto.UsuarioNuevoDTO;
import com.uah.ismael.portal_formula1.service.RolService;
import com.uah.ismael.portal_formula1.service.UsuarioService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
public class AuthenticationController {

    Logger LOG = org.slf4j.LoggerFactory.getLogger(AuthenticationController.class);

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private RolService rolService;

    @GetMapping("/")
    public String home(Model model, Principal principal) {
        String titulo = "Bienvenido a la aplicación de Formula 1";
        if (principal != null) {
            titulo = "Bienvenido, " + principal.getName();
        }
        model.addAttribute("titulo", titulo);
        return "index";
    }

    @GetMapping("/register")
    public String nuevoRegistro(Model model) {
        model.addAttribute("titulo", "Registro de Usuario");
        model.addAttribute("usuario", new UsuarioNuevoDTO());
        model.addAttribute("roles", rolService.getAllRoles());
        return "register";
    }

    @PostMapping("/register")
    public String registro(Model model, UsuarioNuevoDTO usuarioLeido, RedirectAttributes attributes) {
        try {
            System.out.println("Usuario leido: " + usuarioLeido);
            usuarioService.addUsuario(usuarioLeido);
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
            attributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/register";
        }
        attributes.addFlashAttribute("success", "Los datos del registro fueron guardados!");
        System.out.println("Usuario registrado: " + usuarioLeido);
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error, Model model, Principal principal,RedirectAttributes attributes) {
        if (principal != null) {
            return "redirect:/";
        }

        if (error != null) {
            System.out.println("ERROR: " + error);
            model.addAttribute("loginError",
                    "Error al iniciar sesión: Nombre de usuario o contraseña incorrecta, por favor vuelva a intentarlo!");
        }
        model.addAttribute("titulo", "Inicio de sesión");
        return "login";
    }

    @GetMapping("/logout")
    public String logoutPage (HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null){
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/login";
    }
}
