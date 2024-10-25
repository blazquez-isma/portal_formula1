package com.uah.ismael.portal_formula1.model.entity;

import jakarta.persistence.*;

@Entity
public class Coche {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false, unique = true)
    private String codigo;

    @Column(nullable = false)
    private float ERS_curvaLenta;

    @Column(nullable = false)
    private float ERS_curvaMedia;

    @Column(nullable = false)
    private float ERS_curvaRapida;

    @Column(nullable = false)
    private float consumo;

    @ManyToOne
    @JoinColumn(name = "equipoID")
    private Equipo equipo;

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

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public float getERS_curvaLenta() {
        return ERS_curvaLenta;
    }

    public void setERS_curvaLenta(float ERS_curvaLenta) {
        this.ERS_curvaLenta = ERS_curvaLenta;
    }

    public float getERS_curvaMedia() {
        return ERS_curvaMedia;
    }

    public void setERS_curvaMedia(float ERS_curvaMedia) {
        this.ERS_curvaMedia = ERS_curvaMedia;
    }

    public float getERS_curvaRapida() {
        return ERS_curvaRapida;
    }

    public void setERS_curvaRapida(float ERS_curvaRapida) {
        this.ERS_curvaRapida = ERS_curvaRapida;
    }

    public float getConsumo() {
        return consumo;
    }

    public void setConsumo(float consumo) {
        this.consumo = consumo;
    }

    public Equipo getEquipo() {
        return equipo;
    }

    public void setEquipo(Equipo equipo) {
        this.equipo = equipo;
    }
}
