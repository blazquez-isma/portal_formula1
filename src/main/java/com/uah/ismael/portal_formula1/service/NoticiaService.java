package com.uah.ismael.portal_formula1.service;

import com.uah.ismael.portal_formula1.dto.NoticiaDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NoticiaService {

    boolean addNoticia(String titulo, String texto, String imagen, String administradorNombreUsuario);

    boolean updateNoticia(NoticiaDTO noticia);

    boolean deleteNoticia(Long id);

    NoticiaDTO getNoticiaById(Long id);

    Page<NoticiaDTO> getAllNoticias(Pageable pageable);

    Page<NoticiaDTO> getNoticiasByAdministradorNombreUsuario(String administradorNombreUsuario, Pageable pageable);

    public String getNoticiaAnteriorId(Long noticiaId);

    public String getNoticiaSiguienteId(Long noticiaId);
}
