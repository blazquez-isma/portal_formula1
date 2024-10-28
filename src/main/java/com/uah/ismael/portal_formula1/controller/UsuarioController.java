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

    @GetMapping("/registro")
    public String showRegistrationForm(Model model) {
        model.addAttribute("usuario", new UsuarioDTO());
        model.addAttribute("roles", rolService.getAllRoles());
        return "registro";
    }

    @PostMapping("/registro")
    public String registerUser(@ModelAttribute("usuario") UsuarioDTO usuarioDTO) {
        try {
            usuarioService.addUsuario(usuarioDTO);
        } catch (IllegalArgumentException e) {
            return "redirect:/registro/error?error=" + e.getMessage();
        }
        return "redirect:/registro/success?nombreUsuario=" + usuarioDTO.getNombreUsuario();
    }

    @GetMapping("/registro/success")
    public String showSuccessPage(@ModelAttribute("nombreUsuario") String nombreUsuario, Model model) {
        model.addAttribute("nombreUsuario", nombreUsuario);
        return "registro_success";
    }

    @GetMapping("/registro/error")
    public String showErrorPage(@ModelAttribute("error") String error, Model model) {
        model.addAttribute("error", error);
        return "registro_error";
    }



}
