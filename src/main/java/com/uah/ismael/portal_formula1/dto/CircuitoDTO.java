package com.uah.ismael.portal_formula1.dto;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.sql.Date;
import java.util.Comparator;

public class CircuitoDTO {

    private Long id;
    private String nombre;
    private String ciudad;
    private String pais;
    private String trazado;
    private Integer numeroVueltas;
    private Float longitud;
    private Integer curvasLentas;
    private Integer curvasMedias;
    private Integer curvasRapidas;
    private Date fechaCalendario;

    public CircuitoDTO() {
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

    public Date getFechaCalendario() {
        return fechaCalendario;
    }

    public void setFechaCalendario(Date fechaCalendario) {
        this.fechaCalendario = fechaCalendario;
    }

    public static Comparator<CircuitoDTO> getCircuitoPageableComparator(Pageable pageable){
        Sort.Order order = pageable.getSort().iterator().next();
        Comparator<CircuitoDTO> comparator;
        if("ciudad".equals(order.getProperty())){
            comparator = Comparator.comparing(CircuitoDTO::getCiudad);
        }else if("pais".equals(order.getProperty())){
            comparator = Comparator.comparing(CircuitoDTO::getPais);
        }else if("traazado".equals(order.getProperty())){
            comparator = Comparator.comparing(CircuitoDTO::getTrazado);
        }else if("numeroVueltas".equals(order.getProperty())){
            comparator = Comparator.comparing(CircuitoDTO::getNumeroVueltas);
        }else if("longitud".equals(order.getProperty())){
            comparator = Comparator.comparing(CircuitoDTO::getLongitud);
        }else if("curvasLentas".equals(order.getProperty())){
            comparator = Comparator.comparing(CircuitoDTO::getCurvasLentas);
        }else if("curvasMedias".equals(order.getProperty())){
            comparator = Comparator.comparing(CircuitoDTO::getCurvasMedias);
        }else if("curvasRapidas".equals(order.getProperty())){
            comparator = Comparator.comparing(CircuitoDTO::getCurvasRapidas);
        }else if("fechaCalendario".equals(order.getProperty())){
            comparator = Comparator.comparing(CircuitoDTO::getFechaCalendario);
        }else{
            comparator = Comparator.comparing(CircuitoDTO::getNombre);
        }
        if (order.getDirection().isDescending()) {
            comparator = comparator.reversed();
        }
        return comparator;
    }

}
