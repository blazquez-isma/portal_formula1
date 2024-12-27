package com.uah.ismael.portal_formula1.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "circuito")
public class Circuito {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Size(max = 255)
    @NotNull
    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Size(max = 255)
    @NotNull
    @Column(name = "ciudad", nullable = false)
    private String ciudad;

    @Size(max = 255)
    @NotNull
    @Column(name = "pais", nullable = false)
    private String pais;

    @Size(max = 255)
    @Column(name = "trazado")
    private String trazado;

    @NotNull
    @Column(name = "numeroVueltas", nullable = false)
    private Integer numeroVueltas;

    @NotNull
    @Column(name = "longitud", nullable = false)
    private Float longitud;

    @NotNull
    @Column(name = "curvasLentas", nullable = false)
    private Integer curvasLentas;

    @NotNull
    @Column(name = "curvasMedias", nullable = false)
    private Integer curvasMedias;

    @NotNull
    @Column(name = "curvasRapidas", nullable = false)
    private Integer curvasRapidas;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getTrazado() {
        return trazado;
    }

    public void setTrazado(String trazado) {
        this.trazado = trazado;
    }

    public Integer getNumeroVueltas() {
        return numeroVueltas;
    }

    public void setNumeroVueltas(Integer numeroVueltas) {
        this.numeroVueltas = numeroVueltas;
    }

    public Float getLongitud() {
        return longitud;
    }

    public void setLongitud(Float longitud) {
        this.longitud = longitud;
    }

    public Integer getCurvasLentas() {
        return curvasLentas;
    }

    public void setCurvasLentas(Integer curvasLentas) {
        this.curvasLentas = curvasLentas;
    }

    public Integer getCurvasMedias() {
        return curvasMedias;
    }

    public void setCurvasMedias(Integer curvasMedias) {
        this.curvasMedias = curvasMedias;
    }

    public Integer getCurvasRapidas() {
        return curvasRapidas;
    }

    public void setCurvasRapidas(Integer curvasRapidas) {
        this.curvasRapidas = curvasRapidas;
    }

}