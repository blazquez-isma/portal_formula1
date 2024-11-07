package com.uah.ismael.portal_formula1.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ResponsableController {

    @GetMapping("/responsable/home")
    public String responsableHome(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("nombreUsuario", auth.getName());
        return "responsable/responsable_home";
    }
}
