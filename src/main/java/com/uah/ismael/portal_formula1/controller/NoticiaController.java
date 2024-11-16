package com.uah.ismael.portal_formula1.controller;

import com.uah.ismael.portal_formula1.dto.NoticiaDTO;
import com.uah.ismael.portal_formula1.service.NoticiaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class NoticiaController {

    @Autowired
    private NoticiaService noticiaService;

    @GetMapping("/noticias/verNoticias")
    public String viewNoticias(@RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "5") int size,
                               @RequestParam(defaultValue = "nombreUsuario") String sortField,
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

        return "viewNoticias";
    }

    @GetMapping("/noticias/verNoticia")
    public String viewNoticia(@RequestParam("noticiaId") Long noticiaId, Model model) {
        NoticiaDTO noticia = noticiaService.getNoticiaById(noticiaId);
        model.addAttribute("noticia", noticia);
        return "viewNoticia";
    }

    @PostMapping("/noticias/borrarNoticia")
    public String borrarNoticia(@RequestParam("noticiaId") Long noticiaId) {
        noticiaService.deleteNoticia(noticiaId);
        return "redirect:/noticias/verNoticias";
    }

    @GetMapping("/noticias/crearNoticia")
    public String showCrearNoticiaForm(@RequestParam("noticia") NoticiaDTO noticia, Model model) {
        model.addAttribute("noticia", new NoticiaDTO());
        return "redirect:/noticias/crearNoticia";
    }

    @PostMapping("/noticias/crearNoticia")
    public String crearNoticia(@RequestParam("noticia") NoticiaDTO noticia, Model model) {
        String administradorNombreUsuario = SecurityContextHolder.getContext().getAuthentication().getName();
        noticiaService.addNoticia(noticia.getTitulo(), noticia.getTexto(), noticia.getImagen(), administradorNombreUsuario);
        return "redirect:/noticias/verNoticias";
    }

    @PostMapping("/noticias/editarNoticia")
    public String editarNoticia(@RequestParam("noticia") NoticiaDTO noticia, Model model) {
        noticiaService.updateNoticia(noticia);
        return "redirect:/noticias/verNoticia?noticiaId=" + noticia.getId();
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

        return "viewNoticias";
    }

}
