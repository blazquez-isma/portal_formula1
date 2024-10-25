package com.uah.ismael.portal_formula1.model.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "piloto_votacion")
public class PilotoVotacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "votacionID", referencedColumnName = "id")
    private Votacion votacion;

    @ManyToOne
    @JoinColumn(name = "pilotoID", referencedColumnName = "id" )
    private Piloto piloto;

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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