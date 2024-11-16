package com.uah.ismael.portal_formula1.utils;

import java.util.List;

public class Constants {

    public static final List<String> URLS_WITHOUT_AUTHENTICATION = List.of(
            "/",
            "/css/**",
            "/images/**",
            "/js/**",
            "/index",
            "/login",
            "/register",
            "/fragmentos/**"
    );

}
