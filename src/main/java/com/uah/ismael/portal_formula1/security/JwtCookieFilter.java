package com.uah.ismael.portal_formula1.security;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

@Component
public class JwtCookieFilter extends OncePerRequestFilter {

    Logger LOG = org.slf4j.LoggerFactory.getLogger(JwtCookieFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        //LOG.debug("Filtering request for JWT cookie: " + request.getRequestURL());
        Cookie[] cookies = request.getCookies();
        //LOG.debug("Cookies: " + Arrays.toString(Arrays.stream(cookies).map(Cookie::getName).toArray()));
        if (cookies != null) {
            Arrays.stream(cookies)
                    .filter(cookie -> "token".equals(cookie.getName()))
                    .findFirst()
                    .ifPresent(cookie -> {
                        String token = cookie.getValue();
                        request.setAttribute("Authorization", "Bearer " + token);
                    });
        }
//        LOG.debug("Header: " + request.getHeader("Authorization")
//                + " Attribute: " + request.getAttribute("Authorization")
//                + " Cookies: " + Arrays.toString(Arrays.stream(cookies).map(Cookie::getName).toArray())
//                + " Request URL: " + request.getRequestURL()
//                + " Request URI: " + request.getRequestURI()
//                + " Request Method: " + request.getMethod()
//                + " Request Context Path: " + request.getContextPath()
//                + " Request Servlet Path: " + request.getServletPath()
//                + " Request Path Info: " + request.getPathInfo()
//                + " Request Query String: " + request.getQueryString());
        filterChain.doFilter(request, response);
    }
}