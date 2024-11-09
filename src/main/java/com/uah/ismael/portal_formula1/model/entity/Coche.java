package com.uah.ismael.portal_formula1.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "coche")
public class Coche {
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
    @Column(name = "codigo", nullable = false)
    private String codigo;

    @NotNull
    @Column(name = "ERS_curvaLenta", nullable = false)
    private Float ersCurvalenta;

    @NotNull
    @Column(name = "ERS_curvaMedia", nullable = false)
    private Float ersCurvamedia;

    @NotNull
    @Column(name = "ERS_curvaRapida", nullable = false)
    private Float ersCurvarapida;

    @NotNull
    @Column(name = "consumo", nullable = false)
    private Float consumo;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JoinColumn(name = "equipoID")
    private Equipo equipo;

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

    public Float getErsCurvalenta() {
        return ersCurvalenta;
    }

    public void setErsCurvalenta(Float ersCurvalenta) {
        this.ersCurvalenta = ersCurvalenta;
    }

    public Float getErsCurvamedia() {
        return ersCurvamedia;
    }

    public void setErsCurvamedia(Float ersCurvamedia) {
        this.ersCurvamedia = ersCurvamedia;
    }

    public Float getErsCurvarapida() {
        return ersCurvarapida;
    }

    public void setErsCurvarapida(Float ersCurvarapida) {
        this.ersCurvarapida = ersCurvarapida;
    }

    public Float getConsumo() {
        return consumo;
    }

    public void setConsumo(Float consumo) {
        this.consumo = consumo;
    }

    public Equipo getEquipo() {
        return equipo;
    }

    public void setEquipo(Equipo equipoID) {
        this.equipo = equipoID;
    }

}