package com.uah.ismael.portal_formula1.dto;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Comparator;

public class PilotoDTO {

    private Long id;
    private String nombre;
    private String apellidos;
    private String siglas;
    private Integer dorsal;
    private String foto;
    private String pais;
    private String twitter;
    private EquipoDTO equipo;

    public PilotoDTO() {
    }

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

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getSiglas() {
        return siglas;
    }

    public void setSiglas(String siglas) {
        this.siglas = siglas;
    }

    public Integer getDorsal() {
        return dorsal;
    }

    public void setDorsal(Integer dorsal) {
        this.dorsal = dorsal;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getTwitter() {
        return twitter;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public EquipoDTO getEquipo() {
        return equipo;
    }

    public void setEquipo(EquipoDTO equipo) {
        this.equipo = equipo;
    }

    public static Comparator<PilotoDTO> getPilotoPageableComparator(Pageable pageable) {
        // Ordenar la lista
        Sort.Order order = pageable.getSort().iterator().next();
        Comparator<PilotoDTO> comparator;

        if("apellidos".equals(order.getProperty())) {
            comparator = Comparator.comparing(PilotoDTO::getApellidos);
        } else if("siglas".equals(order.getProperty())) {
            comparator = Comparator.comparing(PilotoDTO::getSiglas);
        } else if("dorsal".equals(order.getProperty())) {
            comparator = Comparator.comparing(PilotoDTO::getDorsal);
        } else if("pais".equals(order.getProperty())) {
            comparator = Comparator.comparing(PilotoDTO::getPais);
        } else if("twitter".equals(order.getProperty())) {
            comparator = Comparator.comparing(PilotoDTO::getTwitter);
        }else {
            comparator = Comparator.comparing(PilotoDTO::getNombre);
        }

        if (order.getDirection() == Sort.Direction.DESC) {
            comparator = comparator.reversed();
        }
        return comparator;
    }
}
