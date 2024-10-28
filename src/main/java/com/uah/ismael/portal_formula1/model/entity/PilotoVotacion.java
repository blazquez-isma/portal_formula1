package com.uah.ismael.portal_formula1.model.entity;

import com.uah.ismael.portal_formula1.model.entity.embeddedId.PilotoVotacionId;
import jakarta.persistence.*;

@Entity
@Table(name = "piloto_votacion")
public class PilotoVotacion {

    @EmbeddedId
    private PilotoVotacionId id;

    @ManyToOne
    @MapsId("votacionID")
    @JoinColumn(name = "votacionID", referencedColumnName = "id")
    private Votacion votacion;

    @ManyToOne
    @MapsId("pilotoID")
    @JoinColumn(name = "pilotoID", referencedColumnName = "id" )
    private Piloto piloto;

    public PilotoVotacionId getId() {
        return id;
    }

    public void setId(PilotoVotacionId id) {
        this.id = id;
    }

    public Votacion getVotacion() {
        return votacion;
    }

    public void setVotacion(Votacion votacion) {
        this.votacion = votacion;
    }

    public Piloto getPiloto() {
        return piloto;
    }

    public void setPiloto(Piloto piloto) {
        this.piloto = piloto;
    }
}