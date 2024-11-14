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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    public boolean addNoticia(String titulo, String descripcion, String imagen, String administradorNombreUsuario) {
        Usuario usuario = usuarioRepository.findByNombreUsuario(administradorNombreUsuario);
        if (usuario != null) {
            Noticia noticia = new Noticia();
            noticia.setTitulo(titulo);
            noticia.setTexto(descripcion);
            if (imagen != null) {
                noticia.setImagen(imagen);
            }
            noticia.setPermalink(generatePermalink(titulo));
            noticia.setAdministrador(usuario);
            noticiaRepository.save(noticia);
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
                noticiaToUpdate.setPermalink(generatePermalink(noticia.getTitulo()));
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

    private String generatePermalink(String titulo) {
        return titulo.toLowerCase().replaceAll("[^a-z0-9]+", "-");
    }


}
