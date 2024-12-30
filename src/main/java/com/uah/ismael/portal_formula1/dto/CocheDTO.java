package com.uah.ismael.portal_formula1.dto;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Comparator;

public class CocheDTO {

    private Long id;
    private String nombre;
    private String codigo;
    private Float ersCurvalenta;
    private Float ersCurvamedia;
    private Float ersCurvarapida;
    private Float consumo;

    public CocheDTO() {
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

    public static Comparator<CocheDTO> getCochePageableComparator(Pageable pageable) {
        // Ordenar la lista
        Sort.Order order = pageable.getSort().iterator().next();
        Comparator<CocheDTO> comparator;
        if ("codigo".equals(order.getProperty())) {
            comparator = Comparator.comparing(CocheDTO::getCodigo);
        } else if ("consumo".equals(order.getProperty())) {
            comparator = Comparator.comparing(CocheDTO::getConsumo);
        } else {
            comparator = Comparator.comparing(CocheDTO::getNombre);
        }
        if (order.getDirection() == Sort.Direction.DESC) {
            comparator = comparator.reversed();
        }
        return comparator;
    }

}
