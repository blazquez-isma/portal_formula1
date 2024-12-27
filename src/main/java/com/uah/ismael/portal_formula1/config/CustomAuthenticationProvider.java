package com.uah.ismael.portal_formula1.config;

import com.uah.ismael.portal_formula1.dto.RolDTO;
import com.uah.ismael.portal_formula1.dto.UsuarioDTO;
import com.uah.ismael.portal_formula1.model.entity.Rol;
import com.uah.ismael.portal_formula1.model.entity.Usuario;
import com.uah.ismael.portal_formula1.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public CustomAuthenticationProvider() {
        super();
    }

    @Override
    public Authentication authenticate(final Authentication authentication) throws AuthenticationException {

        final String usuario = authentication.getName();
        String password = authentication.getCredentials().toString();

        System.out.println("Usuario: " + usuario + " Password: " + password);
        UsuarioDTO usuarioLogueado = usuarioService.getUsuarioByEmail(usuario);
        System.out.println("Usuario logueado correo: " + usuarioLogueado);
        if(usuarioLogueado == null) {
            usuarioLogueado = usuarioService.getUsuarioByNombreUsuario(usuario);
        }
        System.out.println("Usuario logueado: " + usuarioLogueado);
        if (usuarioLogueado != null && usuarioLogueado.isActivo() && passwordEncoder.matches(password, usuarioLogueado.getContrasena())) {
            System.out.println("LOGUEADO");
            final List<GrantedAuthority> grantedAuths = new ArrayList<>();
            for (RolDTO rol : usuarioLogueado.getRoles()) {
                grantedAuths.add(new SimpleGrantedAuthority(rol.getNombre()));
            }
            final UserDetails principal = new User(usuario, password, grantedAuths);
            return new UsernamePasswordAuthenticationToken(principal, password, grantedAuths);
        }
        return null;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public boolean supports(final Class authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

}
