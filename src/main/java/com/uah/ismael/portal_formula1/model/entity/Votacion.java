package com.uah.ismael.portal_formula1.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "votacion")
public class Votacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Size(max = 255)
    @NotNull
    @Column(name = "permalink", nullable = false)
    private String permalink;

    @Size(max = 255)
    @NotNull
    @Column(name = "titulo", nullable = false)
    private String titulo;

    @Size(max = 500)
    @Column(name = "descripcion", length = 500)
    private String descripcion;

    @NotNull
    @Column(name = "fechaLimite", nullable = false)
    private Instant fechaLimite;

    @ManyToMany
    @JoinTable(name = "piloto_votacion",
            joinColumns = @JoinColumn(name = "votacionID"),
            inverseJoinColumns = @JoinColumn(name = "pilotoID"))
    private Set<Piloto> pilotos = new LinkedHashSet<>();

    @OneToMany(mappedBy = "votacionID")
    private Set<Voto> votos = new LinkedHashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPermalink() {
        return permalink;
    }

    public void setPermalink(String permalink) {
        this.permalink = permalink;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Instant getFechaLimite() {
        return fechaLimite;
    }

    public void setFechaLimite(Instant fechaLimite) {
        this.fechaLimite = fechaLimite;
    }

    public Set<Piloto> getPilotos() {
        return pilotos;
    }

    public void setPilotos(Set<Piloto> pilotos) {
        this.pilotos = pilotos;
    }

    public Set<Voto> getVotos() {
        return votos;
    }

    public void setVotos(Set<Voto> votos) {
        this.votos = votos;
    }

}