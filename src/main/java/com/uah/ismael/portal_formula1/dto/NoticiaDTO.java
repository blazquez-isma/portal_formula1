package com.uah.ismael.portal_formula1.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Comparator;

public class NoticiaDTO {

    private Long id;

    @NotBlank(message = "El permalink es obligatorio")
    private String permalink;

    @NotBlank(message = "El título es obligatorio")
    private String titulo;

    private String imagen;

    @NotBlank(message = "El texto es obligatorio")
    @Size(min = 500, max = 2000, message = "El texto debe tener entre 500 y 2000 caracteres")
    private String texto;

    public NoticiaDTO() {
    }

    // Getters y Setters
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

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    @Override
    public String toString() {
        return "NoticiaDTO{" +
                "id=" + id +
                ", permalink='" + permalink + '\'' +
                ", titulo='" + titulo + '\'' +
                ", imagen='" + imagen + '\'' +
                ", texto='" + texto + '\'' +
                '}';
    }

    public static Comparator<NoticiaDTO> getNoticiaPageableComparator(Pageable pageable) {
        // Ordenar la lista
        Sort.Order order = pageable.getSort().iterator().next();
        Comparator<NoticiaDTO> comparator = Comparator.comparing(NoticiaDTO::getTitulo);
        if (order.getDirection() == Sort.Direction.DESC) {
            comparator = comparator.reversed();
        }
        return comparator;
    }
}