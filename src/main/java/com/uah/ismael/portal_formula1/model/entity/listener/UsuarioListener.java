package com.uah.ismael.portal_formula1.model.entity.listener;

import com.uah.ismael.portal_formula1.model.entity.Usuario;
import jakarta.persistence.PostLoad;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;



@Component
public class UsuarioListener {

    private static PasswordEncoder passwordEncoder;

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        UsuarioListener.passwordEncoder = passwordEncoder;
    }

    @PrePersist
    @PreUpdate
    public void encryptPassword(Usuario usuario) {
        usuario.setContrasena(passwordEncoder.encode(usuario.getContrasena()));
    }
}