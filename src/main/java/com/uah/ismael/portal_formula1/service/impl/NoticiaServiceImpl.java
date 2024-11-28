package com.uah.ismael.portal_formula1.service.impl;

import com.uah.ismael.portal_formula1.dto.NoticiaDTO;
import com.uah.ismael.portal_formula1.model.entity.Noticia;
import com.uah.ismael.portal_formula1.model.entity.Usuario;
import com.uah.ismael.portal_formula1.model.repository.NoticiaRepository;
import com.uah.ismael.portal_formula1.model.repository.UsuarioRepository;
import com.uah.ismael.portal_formula1.service.NoticiaService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class NoticiaServiceImpl implements NoticiaService {

    private final ModelMapper modelMapper;
    private final NoticiaRepository noticiaRepository;
    private final UsuarioRepository usuarioRepository;

    public NoticiaServiceImpl(ModelMapper modelMapper, NoticiaRepository noticiaRepository, UsuarioRepository usuarioRepository) {
        this.modelMapper = modelMapper;
        this.noticiaRepository = noticiaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public boolean addNoticia(String titulo, String texto, String imagen, String administradorNombreUsuario) {
        Usuario usuario = usuarioRepository.findByNombreUsuario(administradorNombreUsuario);
        if (usuario != null) {
            Noticia noticia = new Noticia();
            noticia.setTitulo(titulo);
            noticia.setTexto(texto);
            if (imagen != null) {
                noticia.setImagen(imagen);
            }
            noticia.setAdministrador(usuario);
            Noticia noticiaSave = noticiaRepository.save(noticia);

            noticiaSave.setPermalink(generatePermalinkById(noticiaSave.getId()));
            noticiaRepository.save(noticiaSave);
            return true;
        }
        return false;
    }

    @Override
    public boolean updateNoticia(NoticiaDTO noticia) {
        Noticia noticiaToUpdate = noticiaRepository.findById(noticia.getId()).orElse(null);
        if (noticiaToUpdate != null) {
            //Comprobar si el titulo ha cambiado y si es así, comprobar si ya existe un permalink con ese titulo
            if (!noticiaToUpdate.getTitulo().equals(noticia.getTitulo())) {
                if (noticiaRepository.existsByTitulo(noticia.getTitulo())
                        || noticiaRepository.existsByPermalink(noticia.getPermalink())) {
                    return false;
                }
                noticiaToUpdate.setTitulo(noticia.getTitulo());
                //noticiaToUpdate.setPermalink(generatePermalinkById(noticia.getId()));
            }
            if (noticia.getTexto() != null && !noticiaToUpdate.getTexto().equals(noticia.getTexto())) {
                noticiaToUpdate.setTexto(noticia.getTexto());
            }
            if (noticia.getImagen() != null && !noticiaToUpdate.getImagen().equals(noticia.getImagen())) {
                noticiaToUpdate.setImagen(noticia.getImagen());
            }
            noticiaRepository.save(noticiaToUpdate);
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteNoticia(Long id) {
        Noticia noticia = noticiaRepository.findById(id).orElse(null);
        if (noticia != null) {
            noticiaRepository.delete(noticia);
            return true;
        }
        return false;
    }

    @Override
    public NoticiaDTO getNoticiaById(Long id) {
        Noticia noticia = noticiaRepository.findById(id).orElse(null);
        if (noticia != null) {
            return modelMapper.map(noticia, NoticiaDTO.class);
        }
        return null;
    }

    @Override
    public Page<NoticiaDTO> getAllNoticias(Pageable pageable) {
        List<NoticiaDTO> noticias = noticiaRepository.findAll().stream()
                .map(noticia -> modelMapper.map(noticia, NoticiaDTO.class))
                .sorted(NoticiaDTO.getNoticiaPageableComparator(pageable))
                .toList();

        int start = Math.min((int) pageable.getOffset(), noticias.size());
        int end = Math.min((start + pageable.getPageSize()), noticias.size());

        return new PageImpl<>(noticias.subList(start, end), pageable, noticias.size());
    }

    @Override
    public Page<NoticiaDTO> getNoticiasByAdministradorNombreUsuario(String administradorNombreUsuario, Pageable pageable) {
        Usuario usuario = usuarioRepository.findByNombreUsuario(administradorNombreUsuario);

        List<NoticiaDTO> noticias = new ArrayList<>();
        if (usuario != null) {
            noticias = noticiaRepository.findByAdministrador(usuario).stream()
                    .map(noticia -> modelMapper.map(noticia, NoticiaDTO.class))
                    .sorted(NoticiaDTO.getNoticiaPageableComparator(pageable))
                    .toList();
        }

        int start = Math.min((int) pageable.getOffset(), noticias.size());
        int end = Math.min((start + pageable.getPageSize()), noticias.size());

        return new PageImpl<>(noticias.subList(start, end), pageable, noticias.size());
    }

    private String generatePermalinkById(Long noticiaId) {
        String dominio = "http://localhost:8080";
        return dominio + "/noticias/verNoticia/" + noticiaId;
    }

    @Override
    public String getNoticiaAnteriorId(Long noticiaId) {
        return noticiaRepository.findFirstByIdLessThanOrderByIdDesc(noticiaId)
                .map(Noticia::getPermalink)
                .orElse(null);
    }

    @Override
    public String getNoticiaSiguienteId(Long noticiaId) {
        return noticiaRepository.findFirstByIdGreaterThanOrderByIdAsc(noticiaId)
                .map(Noticia::getPermalink)
                .orElse(null);
    }
}
