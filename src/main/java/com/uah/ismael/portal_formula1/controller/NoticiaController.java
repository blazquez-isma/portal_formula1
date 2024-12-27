package com.uah.ismael.portal_formula1.controller;

import com.uah.ismael.portal_formula1.dto.NoticiaDTO;
import com.uah.ismael.portal_formula1.service.NoticiaService;
import com.uah.ismael.portal_formula1.service.UploadFileService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.net.MalformedURLException;

@Controller
public class NoticiaController {

    Logger LOG = org.slf4j.LoggerFactory.getLogger(NoticiaController.class);

    @Autowired
    private NoticiaService noticiaService;

    @Autowired
    private UploadFileService uploadFileService;


    @GetMapping("/noticias/verNoticias")
    public String verNoticias(@RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "5") int size,
                               @RequestParam(defaultValue = "titulo") String sortField,
                               @RequestParam(defaultValue = "asc") String sortDir,
                               Model model) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortDir), sortField));
        Page<NoticiaDTO> noticiasPage = noticiaService.getAllNoticias(pageable);

        model.addAttribute("noticiasPage", noticiasPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", noticiasPage.getTotalPages());
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");

        return "noticias/verNoticias";
    }

    @GetMapping("/noticias/verNoticia/{noticiaId}")
    public String viewNoticia(@PathVariable("noticiaId") Long noticiaId,
                              @RequestAttribute(value = "Authorization", required = false) String token,
                              Model model) {
        NoticiaDTO noticia = noticiaService.getNoticiaById(noticiaId);
        model.addAttribute("noticia", noticia);
        String noticiaAnteriorId = noticiaService.getNoticiaAnteriorId(noticiaId);
        String noticiaSiguienteId = noticiaService.getNoticiaSiguienteId(noticiaId);

        model.addAttribute("noticiaAnterior", noticiaAnteriorId);
        model.addAttribute("noticiaSiguiente", noticiaSiguienteId);
//        model.addAttribute("nombreUsuario", jwtTokenUtil.getUsernameFromToken(token));

        return "noticias/verNoticia";
    }

    @PostMapping("/noticias/borrarNoticia")
    public String borrarNoticia(@RequestParam("noticiaId") Long noticiaId, RedirectAttributes attributes) {
        NoticiaDTO noticia = noticiaService.getNoticiaById(noticiaId);

        if (noticia != null && noticia.getImagen() != null && !noticia.getImagen().isEmpty()) {
            if(uploadFileService.delete(noticia.getImagen())) {
                attributes.addFlashAttribute("msg", "Imagen " + noticia.getImagen() + " eliminada con exito!");
            }
        }

        noticiaService.deleteNoticia(noticiaId);
        attributes.addFlashAttribute("msg", "Noticia eliminada con exito!");
        return "redirect:/noticias/verNoticias";
    }

    @GetMapping("/noticias/crearNoticia")
    public String showCrearNoticiaForm(Model model) {
        model.addAttribute("noticia", new NoticiaDTO());
        return "noticias/crearNoticia";
    }

    @PostMapping("/noticias/guardarNoticia")
    public String guardarNoticia(@ModelAttribute("noticia") NoticiaDTO noticia, Model model,
                                 @RequestParam("file") MultipartFile foto, RedirectAttributes attributes) {
        if(foto != null && !foto.isEmpty()) {
            if (noticia.getId() != null && noticia.getId() > 0 && noticia.getImagen() != null
                    && !noticia.getImagen().isEmpty()) {
                uploadFileService.delete(noticia.getImagen());
            }
            String nombreFoto = null;
            try {
                nombreFoto = uploadFileService.copy(foto);
            } catch (IOException e) {
                e.printStackTrace();
            }
            attributes.addFlashAttribute("msg", "Has subido correctamente '" + nombreFoto + "'");
            noticia.setImagen(nombreFoto);
        }

        if (noticia.getId() != null) {
            LOG.debug("Actualizar noticia con id: " + noticia.getId());
            noticiaService.updateNoticia(noticia);
        } else {
            LOG.debug("Crear noticia");
            noticiaService.addNoticia(noticia.getTitulo(), noticia.getTexto(), noticia.getImagen(),
                    SecurityContextHolder.getContext().getAuthentication().getName());
        }

        return "redirect:/noticias/verNoticias";
    }

    @GetMapping("/noticias/editarNoticia/{id}")
    public String editarNoticia(@PathVariable("id") Long noticiaID, Model model) {

        LOG.debug("Editar noticia con id: " + noticiaID);
        NoticiaDTO noticia = noticiaService.getNoticiaById(noticiaID);
        LOG.debug("Noticia: " + noticia);

        model.addAttribute("noticia", noticia);
        return "noticias/crearNoticia";
    }

    @GetMapping("/noticias/verNoticiasByAdmin")
    public String viewNoticiasByAdmin(@RequestParam("administradorNombreUsuario") String administradorNombreUsuario,
                                      @RequestParam(defaultValue = "0") int page,
                                      @RequestParam(defaultValue = "5") int size,
                                      @RequestParam(defaultValue = "nombreUsuario") String sortField,
                                      @RequestParam(defaultValue = "asc") String sortDir,
                                      Model model) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortDir), sortField));
        Page<NoticiaDTO> noticiasPage = noticiaService.getNoticiasByAdministradorNombreUsuario(administradorNombreUsuario, pageable);

        model.addAttribute("noticiasPage", noticiasPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", noticiasPage.getTotalPages());
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");

        return "noticias/verNoticias";
    }

    @GetMapping("/uploads/{filename}")
    public ResponseEntity<Resource> verFoto(@PathVariable String filename) {
        Resource recurso = null;
        LOG.debug("Ver foto: " + filename);
        try {
            recurso = uploadFileService.load(filename);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        LOG.debug("Recurso: " + recurso);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + recurso.getFilename() + "\"")
                .body(recurso);
    }

}
