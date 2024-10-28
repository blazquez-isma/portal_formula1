package com.uah.ismael.portal_formula1.model.entity;

import com.uah.ismael.portal_formula1.model.entity.embeddedId.MiembroEquipoId;
import jakarta.persistence.*;


@Entity
@Table(name = "miembro_equipo")
public class MiembroEquipo {

    @EmbeddedId
    private MiembroEquipoId id;

    @ManyToOne
    @MapsId("usuarioID")
    @JoinColumn(name = "usuarioID", referencedColumnName = "id")
    private Usuario usuario;

    @ManyToOne
    @MapsId("equipoID")
    @JoinColumn(name = "equipoID", referencedColumnName = "id")
    private Equipo equipo;

    public MiembroEquipoId getId() {
        return id;
    }

    public void setId(MiembroEquipoId id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Equipo getEquipo() {
        return equipo;
    }

    public void setEquipo(Equipo equipo) {
        this.equipo = equipo;
    }
}
