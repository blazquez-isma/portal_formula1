package com.uah.ismael.portal_formula1.controller;

import com.uah.ismael.portal_formula1.dto.NoticiaDTO;
import com.uah.ismael.portal_formula1.paginator.PageUtil;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.net.MalformedURLException;
import java.time.LocalDate;

@Controller
@RequestMapping("/noticias")
public class NoticiaController {

    Logger LOG = org.slf4j.LoggerFactory.getLogger(NoticiaController.class);

    @Autowired
    private NoticiaService noticiaService;

    @Autowired
    private UploadFileService uploadFileService;


    @GetMapping
    public String verNoticias(@RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "5") int size,
                               @RequestParam(defaultValue = "titulo") String sortField,
                               @RequestParam(defaultValue = "asc") String sortDir,
                               Model model) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortDir), sortField));
        Page<NoticiaDTO> noticiasPage = noticiaService.getAllNoticias(pageable);

        model.addAttribute("titulo", "Listado de Noticias");
        PageUtil.addPaginationAttributes(model, noticiasPage, page, sortField, sortDir);
        return "noticias/listNoticias";
    }

    @GetMapping("/verNoticia/{noticiaId}")
    public String verNoticia(@PathVariable("noticiaId") Long noticiaId,
                             Model model) {
        NoticiaDTO noticia = noticiaService.getNoticiaById(noticiaId);
        model.addAttribute("noticia", noticia);
        String noticiaAnteriorId = noticiaService.getNoticiaAnteriorId(noticiaId);
        String noticiaSiguienteId = noticiaService.getNoticiaSiguienteId(noticiaId);

        model.addAttribute("titulo", "Noticia");
        model.addAttribute("noticiaAnterior", noticiaAnteriorId);
        model.addAttribute("noticiaSiguiente", noticiaSiguienteId);

        return "noticias/seeNoticia";
    }

    @GetMapping("/borrarNoticia/{id}")
    public String borrarNoticia(@PathVariable("id") Long noticiaId, RedirectAttributes attributes) {
        NoticiaDTO noticia = noticiaService.getNoticiaById(noticiaId);

        if (noticia != null && noticia.getImagen() != null && !noticia.getImagen().isEmpty()) {
            if(uploadFileService.delete(noticia.getImagen())) {
                attributes.addFlashAttribute("msg", "Imagen " + noticia.getImagen() + " eliminada con exito!");
            }
        }

        noticiaService.deleteNoticia(noticiaId);
        attributes.addFlashAttribute("msg", "Noticia eliminada con exito!");
        return "redirect:/noticias";
    }

    @GetMapping("/crearNoticia")
    public String formularioCrearNoticia(Model model) {
        model.addAttribute("titulo", "Crear Noticia");
        model.addAttribute("noticia", new NoticiaDTO());
        return "noticias/createNoticia";
    }

    @PostMapping("/guardarNoticia")
    public String guardarNoticia(@ModelAttribute("noticia") NoticiaDTO noticia, Model model,
                                 @RequestParam("file") MultipartFile foto, RedirectAttributes attributes) {

        if(foto != null && !foto.isEmpty()) {
            if (noticia.getId() != null && noticia.getId() > 0 && noticia.getImagen() != null
                    && !noticia.getImagen().isEmpty()) {
                uploadFileService.delete(noticia.getImagen());
            }
            String nombreImagen = null;
            try {
                nombreImagen = uploadFileService.copy(foto);
            } catch (IOException e) {
                e.printStackTrace();
            }
            attributes.addFlashAttribute("msg", "Has subido correctamente '" + nombreImagen + "'");
            noticia.setImagen(nombreImagen);
        }

        noticia.setFecha(LocalDate.now());
        if (noticia.getId() != null && noticia.getId() > 0) {
            noticiaService.updateNoticia(noticia);
        } else {
            noticiaService.addNoticia(noticia, SecurityContextHolder.getContext().getAuthentication().getName());
        }

        return "redirect:/noticias";
    }

    @GetMapping("/editarNoticia/{id}")
    public String editarNoticia(@PathVariable("id") Long noticiaID, Model model) {
        NoticiaDTO noticia = noticiaService.getNoticiaById(noticiaID);

        model.addAttribute("titulo", "Editar Noticia");
        model.addAttribute("noticia", noticia);
        return "noticias/createNoticia";
    }

    @GetMapping("/verNoticiasByAdmin")
    public String verNoticiasByAdmin(@RequestParam("administradorNombreUsuario") String administradorNombreUsuario,
                                     @RequestParam(defaultValue = "0") int page,
                                     @RequestParam(defaultValue = "5") int size,
                                     @RequestParam(defaultValue = "nombreUsuario") String sortField,
                                     @RequestParam(defaultValue = "asc") String sortDir,
                                     Model model) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortDir), sortField));
        Page<NoticiaDTO> noticiasPage = noticiaService.getNoticiasByAdministradorNombreUsuario(administradorNombreUsuario, pageable);

        model.addAttribute("titulo", "Listado de Noticias");
        PageUtil.addPaginationAttributes(model, noticiasPage, page, sortField, sortDir);
        return "noticias/listNoticias";
    }

    @GetMapping("/verImagen/{filename}")
    public ResponseEntity<Resource> verFoto(@PathVariable String filename) {
        Resource recurso = null;
        try {
            recurso = uploadFileService.load(filename);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + recurso.getFilename() + "\"")
                .body(recurso);
    }

}
