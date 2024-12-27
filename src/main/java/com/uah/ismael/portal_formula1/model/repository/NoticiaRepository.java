package com.uah.ismael.portal_formula1.model.repository;

import com.uah.ismael.portal_formula1.model.entity.Noticia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NoticiaRepository extends JpaRepository<Noticia, Long> {

    Noticia findByPermalink(String permalink);

    Noticia findByTitulo(String titulo);

    List<Noticia> findByAdministrador_Id(Long administradorID);

    Noticia findByTituloContaining(String titulo);

    Noticia findByTextoContaining(String texto);

    boolean existsByPermalink(String permalink);

    boolean existsByTitulo(String titulo);

    Optional<Noticia> findFirstByIdLessThanOrderByIdDesc(Long id);
    Optional<Noticia> findFirstByIdGreaterThanOrderByIdAsc(Long id);
}