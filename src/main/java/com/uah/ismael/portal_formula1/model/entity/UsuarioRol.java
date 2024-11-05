package com.uah.ismael.portal_formula1.model.entity;

import com.uah.ismael.portal_formula1.model.entity.embeddedId.UsuarioRolId;
import jakarta.persistence.*;

@Entity
@Table(name = "usuario_roles")
public class UsuarioRol {

    @EmbeddedId
    private UsuarioRolId id;

    @ManyToOne
    @MapsId("usuarioID")
    @JoinColumn(name = "usuarioID", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @MapsId("rolID")
    @JoinColumn(name = "rolID", nullable = false)
    private Rol rol;

    public UsuarioRolId getId() {
        return id;
    }

    public void setId(UsuarioRolId id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }
}