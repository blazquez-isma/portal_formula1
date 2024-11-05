package com.uah.ismael.portal_formula1.model.entity.embeddedId;


import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class UsuarioRolId implements Serializable {
    private Integer usuarioID;
    private Integer rolID;

    public Integer getUsuarioID() {
        return usuarioID;
    }

    public void setUsuarioID(Integer usuarioID) {
        this.usuarioID = usuarioID;
    }

    public Integer getRolID() {
        return rolID;
    }

    public void setRolID(Integer rolID) {
        this.rolID = rolID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UsuarioRolId that = (UsuarioRolId) o;
        return Objects.equals(usuarioID, that.usuarioID) && Objects.equals(rolID, that.rolID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(usuarioID, rolID);
    }
}