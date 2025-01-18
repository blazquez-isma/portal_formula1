package com.uah.ismael.portal_formula1.utils;

public class Constants {

    public static final String[] PUBLIC_PATHS = {
            "/js/**", "/css/**", "/images/**",
            "/", "/login", "/registro",
            "/noticias", "/noticias/verNoticia/**", "/noticias/verImagen/**",
            "/equipos", "/equipos/verEquipo/**", "/equipos/verImagen/**",
            "/pilotos/**", "/pilotos/verImagen/**",
            "/votaciones", "votaciones/activas", "votaciones/finalizadas", "/votaciones/verVotacion/**", "/votaciones/verImagen/**",
            "/votaciones/votar/**",
            "/circuitos/verCircuito/**", "/circuitos/verImagen/**", "/circuitos/verCalendario"
    };

    public static final String NOTICIAS = "noticias";
    public static final String EQUIPOS = "equipos";
    public static final String PILOTOS = "pilotos";
    public static final String CIRCUITOS = "circuitos";
    public static final String UPLOADS_FOLDER = "uploads";
    public static final String NOTICIAS_PATH = "uploads/noticias";
    public static final String EQUIPOS_PATH = "uploads/equipos";
    public static final String PILOTOS_PATH = "uploads/pilotos";
    public static final String CIRCUITOS_PATH = "uploads/circuitos";

    public static final String DEFAULT_SIZE = "6";
    public static final double LIMITE_ENERGIA_VUELTA = 0.6;
    public static final double CAPACIDAD_BATERIA = 1.20;
}
