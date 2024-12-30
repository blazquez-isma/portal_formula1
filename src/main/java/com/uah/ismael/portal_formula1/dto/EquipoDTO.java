package com.uah.ismael.portal_formula1.dto;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class EquipoDTO {

    private Long id;
    private String nombre;
    private String logo;
    private String twitter;
//    private List<Long> coches;
//    private List<Long> responsables;
//    private List<Long> pilotos;

    public EquipoDTO() {
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

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getTwitter() {
        return twitter;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public static Comparator<EquipoDTO> getEquipoPageableComparator(Pageable pageable) {
        // Ordenar la lista
        Sort.Order order = pageable.getSort().iterator().next();
        Comparator<EquipoDTO> comparator;
        if ("twitter".equals(order.getProperty())) {
            comparator = Comparator.comparing(EquipoDTO::getTwitter);
        } else {
            comparator = Comparator.comparing(EquipoDTO::getNombre);
        }
        if (order.getDirection() == Sort.Direction.DESC) {
            comparator = comparator.reversed();
        }
        return comparator;
    }

}
