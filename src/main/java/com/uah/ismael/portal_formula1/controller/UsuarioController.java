package com.uah.ismael.portal_formula1.controller;

import com.uah.ismael.portal_formula1.dto.UsuarioNuevoDTO;
import com.uah.ismael.portal_formula1.security.JwtTokenUtil;
import com.uah.ismael.portal_formula1.service.RolService;
import com.uah.ismael.portal_formula1.service.UsuarioService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UsuarioController {

    Logger LOG = org.slf4j.LoggerFactory.getLogger(UsuarioController.class);

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private RolService rolService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("usuario", new UsuarioNuevoDTO());
        model.addAttribute("roles", rolService.getAllRoles());
        return "register/register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("usuario") UsuarioNuevoDTO usuarioDTO,
                               RedirectAttributes redirectAttributes) {
        try {
            usuarioService.addUsuario(usuarioDTO);
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/register/register_error";
        }
        redirectAttributes.addFlashAttribute("nombreUsuario", usuarioDTO.getNombreUsuario());
        return "redirect:/register/register_success";
    }

    @GetMapping("/register/register_success")
    public String showSuccessPage(@ModelAttribute("nombreUsuario") String nombreUsuario, Model model) {
        model.addAttribute("nombreUsuario", nombreUsuario);
        return "register/register_success";
    }

    @GetMapping("/register/register_error")
    public String showErrorPage(@ModelAttribute("error") String error, Model model) {
        model.addAttribute("error", error);
        return "register/register_error";
    }

    @GetMapping("/login")
    public String showLogin(Model model, @ModelAttribute("error") String error) {
        if ("true".equals(error)) {
            model.addAttribute("loginError", "Usuario o contraseña no son válidos");
        }
        return "login";
    }

    @PostMapping("/login")
    public String loginUser(@RequestParam String nombreUsuario, @RequestParam String contrasena,
                            Model model, HttpServletResponse response) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(nombreUsuario, contrasena));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            UserDetails userDetails = userDetailsService.loadUserByUsername(nombreUsuario);

            LOG.debug("User: " + userDetails.getUsername());
            String role = userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .findFirst()
                    .orElse("");

            generateToken(response, userDetails);

            if ("ROLE_Administrador".equals(role)) {
                return "redirect:/admin/home";
            } else if ("ROLE_Responsable".equals(role)) {
                return "redirect:/responsable/home";
            } else {
                model.addAttribute("loginError", "Rol no reconocido");
                return "login";
            }
        } catch (Exception e) {
            LOG.error("ERROR: " + e.getMessage());
            model.addAttribute("loginError", "Usuario o contraseña no son válidos");
            return "login";
        }
    }

    @GetMapping("/login?logout")
    public String logout() {
        return "login";
    }


    private void generateToken(HttpServletResponse response, UserDetails userDetails) {
        String token = jwtTokenUtil.generateToken(userDetails);
        response.setHeader("Authorization", "Bearer " + token);
        Cookie cookie = new Cookie("token", token);
        cookie.setHttpOnly(true);
        //cookie.setSecure(true); // Use true if using HTTPS
        cookie.setPath("/");
        cookie.setMaxAge((int) jwtTokenUtil.getJwtExpirationInMs() / 1000);
        response.addCookie(cookie);
        LOG.debug("Controller Token: " + token);
    }

}
