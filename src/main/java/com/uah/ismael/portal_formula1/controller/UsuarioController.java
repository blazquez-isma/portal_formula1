package com.uah.ismael.portal_formula1.controller;

import com.uah.ismael.portal_formula1.dto.UsuarioDTO;
import com.uah.ismael.portal_formula1.service.RolService;
import com.uah.ismael.portal_formula1.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private RolService rolService;

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("usuario", new UsuarioDTO());
        model.addAttribute("roles", rolService.getAllRoles());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("usuario") UsuarioDTO usuarioDTO) {
        try {
            usuarioService.addUsuario(usuarioDTO);
        } catch (IllegalArgumentException e) {
            return "redirect:/register_error?error=" + e.getMessage();
        }
        return "redirect:/register_success?nombreUsuario=" + usuarioDTO.getNombreUsuario();
    }

    @GetMapping("/register_success")
    public String showSuccessPage(@ModelAttribute("nombreUsuario") String nombreUsuario, Model model) {
        model.addAttribute("nombreUsuario", nombreUsuario);
        return "register_success";
    }

    @GetMapping("/register_error")
    public String showErrorPage(@ModelAttribute("error") String error, Model model) {
        model.addAttribute("error", error);
        return "register_error";
    }

    @GetMapping("/login")
    public String shoLogin() {
        return "login";
    }

    @PostMapping("/login")
    public String loginUser(@ModelAttribute("nombreUsuario") String nombreUsuario, @ModelAttribute("contrasena") String contrasena) {
        return "redirect:/login_success";
    }

}
