package com.uah.ismael.portal_formula1.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "simulacion")
public class Simulacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Size(max = 255)
    @NotNull
    @Column(name = "tipo", nullable = false)
    private String tipo;

    @NotNull
    @Column(name = "resultado", nullable = false)
    private Float resultado;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "cocheID")
    private Coche coche;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "circuitoID")
    private Circuito circuito;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "responsableID")
    private Usuario responsable;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Float getResultado() {
        return resultado;
    }

    public void setResultado(Float resultado) {
        this.resultado = resultado;
    }

    public Coche getCoche() {
        return coche;
    }

    public void setCoche(Coche cocheID) {
        this.coche = cocheID;
    }

    public Circuito getCircuito() {
        return circuito;
    }

    public void setCircuito(Circuito circuitoID) {
        this.circuito = circuitoID;
    }

    public Usuario getResponsable() {
        return responsable;
    }

    public void setResponsable(Usuario responsableID) {
        this.responsable = responsableID;
    }

}