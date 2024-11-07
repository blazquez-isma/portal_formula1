package com.uah.ismael.portal_formula1.controller;

import com.uah.ismael.portal_formula1.dto.UsuarioDTO;
import com.uah.ismael.portal_formula1.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class AdminController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/admin/home")
    public String adminHome(Model model) {
        System.out.println("admin home");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("nombreUsuario", auth.getName());
        return "admin/admin_home";
    }

    @GetMapping("/admin/activate_users")
    public String showUsersToValidate(Model model) {
        List<UsuarioDTO> usuariosNoActivos = usuarioService.getUsuariosNoActivos();
        model.addAttribute("usuariosNoActivos", usuariosNoActivos);
        return "admin/activate_users";
    }

    @PostMapping("/admin/activate_user")
    public String validateUser(@RequestParam("userId") Long userId) {
        usuarioService.activateUser(userId);
        return "redirect:/admin/activate_users";
    }
}
