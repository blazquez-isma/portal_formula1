package com.uah.ismael.portal_formula1.controller;

import com.uah.ismael.portal_formula1.dto.UsuarioNuevoDTO;
import com.uah.ismael.portal_formula1.security.JwtTokenUtil;
import com.uah.ismael.portal_formula1.service.RolService;
import com.uah.ismael.portal_formula1.service.UsuarioService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
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
public class AuthenticationController {

    Logger LOG = org.slf4j.LoggerFactory.getLogger(AuthenticationController.class);

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
        return "/register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("usuario") UsuarioNuevoDTO usuarioDTO,
                               RedirectAttributes redirectAttributes) {
        try {
            usuarioService.addUsuario(usuarioDTO);
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/register";
        }
        redirectAttributes.addFlashAttribute("success", usuarioDTO.getNombreUsuario() + " añadido con éxito");
        return "redirect:/register";
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

            LOG.debug("Role: " + role);
            if ("ROLE_Administrador".equals(role) || "ROLE_Responsable".equals(role)) {
                return "redirect:/usuario/home?nombreUsuario=" + nombreUsuario;
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

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        // Invalidate the session
        SecurityContextHolder.clearContext();
        request.getSession().invalidate();

        // Remove the JWT cookie
        Cookie cookie = new Cookie("token", null);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        return "redirect:/login";
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
