package com.uah.ismael.portal_formula1.dto;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class VotacionDTO {

    private Long id;

    private String permalink;

    private String titulo;

    private String descripcion;

    private Timestamp fechaLiminte;

    List<PilotoDTO> pilotos;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPermalink() {
        return permalink;
    }

    public void setPermalink(String permalink) {
        this.permalink = permalink;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Timestamp getFechaLiminte() {
        return fechaLiminte;
    }

    public void setFechaLiminte(Timestamp fechaLiminte) {
        this.fechaLiminte = fechaLiminte;
    }

    public List<PilotoDTO> getPilotos() {
        return pilotos;
    }

    public void setPilotos(List<PilotoDTO> pilotos) {
        this.pilotos = pilotos;
    }

    public void addPiloto(PilotoDTO piloto) {
        if (this.pilotos == null) {
            this.pilotos = new ArrayList<>();
        }
        this.pilotos.add(piloto);
    }

    public void removePiloto(PilotoDTO piloto) {
        if (this.pilotos != null) {
            this.pilotos.remove(piloto);
        }
    }

    public VotacionDTO() {
    }

    @Override
    public String toString() {
        return "VotacionDTO{" +
                "id=" + id +
                ", permalink='" + permalink + '\'' +
                ", titulo='" + titulo + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", fechaLiminte=" + fechaLiminte +
                ", pilotos=" + pilotos +
                '}';
    }

    public static Comparator<VotacionDTO> getVotacionPageableComparator(Pageable pageable) {
        // Ordenar la lista
        Sort.Order order = pageable.getSort().iterator().next();
        Comparator<VotacionDTO> comparator;
        if(order.getProperty().equals("fechaLiminte")) {
            comparator = Comparator.comparing(VotacionDTO::getFechaLiminte);
        } else {
            comparator = Comparator.comparing(VotacionDTO::getTitulo);
        }
        if (order.getDirection() == Sort.Direction.DESC) {
            comparator = comparator.reversed();
        }
        return comparator;
    }
}
