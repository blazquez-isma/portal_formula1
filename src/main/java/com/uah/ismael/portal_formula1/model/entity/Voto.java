package com.uah.ismael.portal_formula1.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "voto")
public class Voto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Size(max = 255)
    @NotNull
    @Column(name = "nombreVotante", nullable = false)
    private String nombreVotante;

    @Size(max = 255)
    @NotNull
    @Column(name = "email", nullable = false)
    private String email;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "pilotoID", nullable = false)
    private Piloto piloto;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "votacionID", nullable = false)
    private Votacion votacion;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombreVotante() {
        return nombreVotante;
    }

    public void setNombreVotante(String nombreVotante) {
        this.nombreVotante = nombreVotante;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Piloto getPiloto() {
        return piloto;
    }

    public void setPiloto(Piloto pilotoID) {
        this.piloto = pilotoID;
    }

    public Votacion getVotacion() {
        return votacion;
    }

    public void setVotacion(Votacion votacionID) {
        this.votacion = votacionID;
    }

}