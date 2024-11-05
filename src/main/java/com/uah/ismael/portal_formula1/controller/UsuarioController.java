package com.uah.ismael.portal_formula1.controller;

import com.uah.ismael.portal_formula1.dto.UsuarioDTO;
import com.uah.ismael.portal_formula1.security.JwtTokenUtil;
import com.uah.ismael.portal_formula1.service.CustomUserDetailsService;
import com.uah.ismael.portal_formula1.service.RolService;
import com.uah.ismael.portal_formula1.service.UsuarioService;
import com.uah.ismael.portal_formula1.utils.Constants;
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
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserDetailsService userDetailsService;

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
    public String registerUser(@ModelAttribute("usuario") UsuarioDTO usuarioDTO,
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
        return "register_success";
    }

    @GetMapping("/register/register_error")
    public String showErrorPage(@ModelAttribute("error") String error, Model model) {
        model.addAttribute("error", error);
        return "register_error";
    }

    @GetMapping("/login")
    public String showLogin(Model model, @ModelAttribute("error") String error) {
        if ("true".equals(error)) {
            model.addAttribute("loginError", "Usuario o contraseña no son válidos PRINGAO");
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
            LOG.debug("PASSWORD: " + contrasena + " AUTH: " + authentication.getName());
            UserDetails userDetails = userDetailsService.loadUserByUsername(nombreUsuario);
            //String token = jwtTokenUtil.generateToken(userDetails);

            // Quiero coger todas las autoridades del usuario y mostrarlas separadas por ,
            String authorities = userDetails.getAuthorities().stream()
                    .map(a -> a.getAuthority())
                    .reduce("", (a, b) -> a + ", " + b);

            LOG.debug("USER DETAILS: " + userDetails.getUsername() + " " + userDetails.getPassword()
                    + " Authorities: " + authorities + " IsEnabled " + userDetails.isEnabled()
                    + " isAccountNonExpired" + userDetails.isAccountNonExpired()
                    + " isAccountNonLocked" + userDetails.isAccountNonLocked()
                    + " isCredentialsNonExpired" + userDetails.isCredentialsNonExpired());
            //LOG.debug("TOKEN: " + token);


//            Cookie cookie = new Cookie("token", token.replace("Bearer ", ""));
//            cookie.setHttpOnly(true);
//            cookie.setPath("/");
//            response.addCookie(cookie);

            //LOG.debug(" Cookie: " + cookie.getValue());

            String role = userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .findFirst()
                    .orElse("");

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

//    @PostMapping("/login")
//    public String loginUser(@ModelAttribute("nombreUsuario") String nombreUsuario,
//                            @ModelAttribute("contrasena") String contrasena, Model model) {
//        try {
//            if (usuarioService.verifyCredentials(nombreUsuario, contrasena)) {
//                UserDetails userDetails = customUserDetailsService.loadUserByUsername(nombreUsuario);
//                userDetails.getAuthorities().forEach(a -> System.out.println(a.getAuthority()));
//                UsernamePasswordAuthenticationToken authToken =
//                        new UsernamePasswordAuthenticationToken(userDetails, contrasena, userDetails.getAuthorities());
//                SecurityContextHolder.getContext().setAuthentication(authToken);
//                String role = userDetails.getAuthorities().stream()
//                        .map(GrantedAuthority::getAuthority)
//                        .findFirst()
//                        .orElse("");
//                System.out.println(role);
//                if ("ROLE_Administrador".equals(role)) {
//                    return "redirect:/admin/home";
//                } else {
//                    return "redirect:/resp_equipo/home";
//                }
//            } else {
//                model.addAttribute("loginError", "Usuario o contraseña no son válidos");
//                return "login";
//            }
//        } catch (Exception e) {
//            model.addAttribute("loginError", "Ocurrió un error durante el inicio de sesión");
//            return "login";
//        }
//    }

}
