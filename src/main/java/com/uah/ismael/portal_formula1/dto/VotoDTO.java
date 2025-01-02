package com.uah.ismael.portal_formula1.dto;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Comparator;

public class VotoDTO {

    private Long id;

    private String nombreVotante;

    private String email;

    private PilotoDTO piloto;

    private VotacionDTO votacion;

    public VotacionDTO getVotacion() {
        return votacion;
    }

    public void setVotacion(VotacionDTO votacion) {
        this.votacion = votacion;
    }

    public PilotoDTO getPiloto() {
        return piloto;
    }

    public void setPiloto(PilotoDTO piloto) {
        this.piloto = piloto;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNombreVotante() {
        return nombreVotante;
    }

    public void setNombreVotante(String nombreVotante) {
        this.nombreVotante = nombreVotante;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public VotoDTO() {
    }

    @Override
    public String toString() {
        return "VotoDTO{" +
                "id=" + id +
                ", nombreVotante='" + nombreVotante + '\'' +
                ", email='" + email + '\'' +
                ", piloto=" + piloto +
                ", votacion=" + votacion +
                '}';
    }

    public static Comparator<VotoDTO> getVotoPageableComparator(Pageable pageable) {
        // Ordenar la lista
        Sort.Order order = pageable.getSort().iterator().next();
        Comparator<VotoDTO> comparator;
        if (order.getProperty().equals("nombreVotante")) {
            comparator = Comparator.comparing(VotoDTO::getNombreVotante);
        } else {
            comparator = Comparator.comparing(VotoDTO::getEmail);
        }
        if (order.getDirection() == Sort.Direction.DESC) {
            comparator = comparator.reversed();
        }
        return comparator;
    }

}
