package com.uah.ismael.portal_formula1.controller;

import com.uah.ismael.portal_formula1.dto.RolDTO;
import com.uah.ismael.portal_formula1.dto.UsuarioDTO;
import com.uah.ismael.portal_formula1.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/home")
    public String adminHome(Model model) {
        System.out.println("admin home");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("nombreUsuario", auth.getName());
        return "admin/admin_home";
    }

    @GetMapping("/show_users")
    public String showAllUsers(@RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "5") int size,
                               @RequestParam(defaultValue = "nombreUsuario") String sortField,
                               @RequestParam(defaultValue = "asc") String sortDir,
                               Model model) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortDir), sortField));
        Page<UsuarioDTO> usuarioPage = usuarioService.getAllUsuarios(pageable);

        model.addAttribute("userPage", usuarioPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", usuarioPage.getTotalPages());
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");

        return "admin/show_users";
    }

    @PostMapping("/activate_user")
    public String validateUser(@RequestParam("userId") Long userId) {
        usuarioService.activarUsuario(userId);
        return "redirect:/admin/show_users";
    }

    @PostMapping("/delete_user")
    public String deleteUser(@RequestParam("userId") Long userId) {
        usuarioService.borrarUsuario(userId);
        return "redirect:/admin/show_users";
    }

}
