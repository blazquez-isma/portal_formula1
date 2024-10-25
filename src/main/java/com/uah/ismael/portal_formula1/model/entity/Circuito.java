package com.uah.ismael.portal_formula1.model.entity;

import jakarta.persistence.*;

@Entity
public class Circuito {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String ciudad;

    @Column(nullable = false)
    private String pais;

    private String trazado;

    @Column(nullable = false)
    private int numeroVueltas;

    @Column(nullable = false)
    private float longitud;

    @Column(nullable = false)
    private int curvasLentas;

    @Column(nullable = false)
    private int curvasMedias;

    @Column(nullable = false)
    private int curvasRapidas;

    // Getters y Setters
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

    public int getNumeroVueltas() {
        return numeroVueltas;
    }

    public void setNumeroVueltas(int numeroVueltas) {
        this.numeroVueltas = numeroVueltas;
    }

    public float getLongitud() {
        return longitud;
    }

    public void setLongitud(float longitud) {
        this.longitud = longitud;
    }

    public int getCurvasLentas() {
        return curvasLentas;
    }

    public void setCurvasLentas(int curvasLentas) {
        this.curvasLentas = curvasLentas;
    }

    public int getCurvasMedias() {
        return curvasMedias;
    }

    public void setCurvasMedias(int curvasMedias) {
        this.curvasMedias = curvasMedias;
    }

    public int getCurvasRapidas() {
        return curvasRapidas;
    }

    public void setCurvasRapidas(int curvasRapidas) {
        this.curvasRapidas = curvasRapidas;
    }
}