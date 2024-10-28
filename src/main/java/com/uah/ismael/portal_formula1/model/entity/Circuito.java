package com.uah.ismael.portal_formula1.model.entity;

import jakarta.persistence.*;

@Entity
public class Circuito {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String ciudad;

    @Column(nullable = false)
    private String pais;

    private String trazado;

    @Column(name = "numeroVueltas", nullable = false)
    private int numeroVueltas;

    @Column(nullable = false)
    private float longitud;

    @Column(name = "curvasLentas", nullable = false)
    private int curvasLentas;

    @Column(name = "curvasMedias", nullable = false)
    private int curvasMedias;

    @Column(name = "curvasRapidas", nullable = false)
    private int curvasRapidas;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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