package com.uah.ismael.portal_formula1.model.repository;

import com.uah.ismael.portal_formula1.model.entity.Noticia;
import com.uah.ismael.portal_formula1.model.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoticiaRepository extends JpaRepository<Noticia, Long> {

    Noticia findByPermalink(String permalink);

    Noticia findByTitulo(String titulo);

    List<Noticia> findByAdministrador(Usuario administradorID);

    Noticia findByTituloContaining(String titulo);

    Noticia findByTextoContaining(String texto);

    boolean existsByPermalink(String permalink);

    boolean existsByTitulo(String titulo);
}