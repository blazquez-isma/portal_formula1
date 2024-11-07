package com.uah.ismael.portal_formula1.model.entity.embeddedId;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class MiembroEquipoId implements Serializable {
    private Long usuarioID;
    private Long equipoID;

    public Long getUsuarioID() {
        return usuarioID;
    }
    public void setUsuarioID(Long usuarioID) {
        this.usuarioID = usuarioID;
    }

    public Long getEquipoID() {
        return equipoID;
    }
    public void setEquipoID(Long equipoID) {
        this.equipoID = equipoID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        MiembroEquipoId that = (MiembroEquipoId) o;
        return Objects.equals(usuarioID, that.usuarioID) && Objects.equals(equipoID, that.equipoID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(usuarioID, equipoID);
    }
}