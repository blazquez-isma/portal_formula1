package com.uah.ismael.portal_formula1.security;

import com.uah.ismael.portal_formula1.service.CustomUserDetailsService;
import com.uah.ismael.portal_formula1.utils.Constants;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    Logger LOG = org.slf4j.LoggerFactory.getLogger(JwtRequestFilter.class);

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        // Rutas que no requieren autenticación JWT
        if(isURLWithoutAuth(request.getRequestURI())) {
            chain.doFilter(request, response);
            return;
        }

        final String requestTokenHeader = (String) request.getAttribute("Authorization");
        String username = null;
        String jwtToken = null;
        LOG.debug("Request Token Header: " + requestTokenHeader);
        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            jwtToken = requestTokenHeader.substring(7);
            try {
                username = jwtTokenUtil.getUsernameFromToken(jwtToken);
            } catch (IllegalArgumentException e) {
                LOG.debug("Unable to get JWT Token");
            } catch (ExpiredJwtException e) {
                LOG.debug("JWT Token has expired");
                response.sendRedirect("/login?error=sessionExpired");
                return;
            }
        } else {
            logger.warn("JWT Token does not begin with Bearer String");
        }

        LOG.debug("Username: " + username);

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.customUserDetailsService.loadUserByUsername(username);
            LOG.debug("Validating token for user: " + userDetails.getUsername());
            if (jwtTokenUtil.validateToken(jwtToken, userDetails)) {
                LOG.debug("Token is valid");
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            } else {
                LOG.debug("Token is invalid");
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
            }
        }
        chain.doFilter(request, response);
    }

    private static boolean isURLWithoutAuth(String requestURI) {
        return Constants.URLS_WITHOUT_AUTHENTICATION.stream().anyMatch(url -> {
            String urlWithoutSlash = (!url.equals("/") && url.endsWith("/")) ? url.substring(0, url.length() - 1) : url;
            return urlWithoutSlash.endsWith("**") ? requestURI.startsWith(urlWithoutSlash.replace("/**", "")) : requestURI.equals(urlWithoutSlash);
        });
    }
}