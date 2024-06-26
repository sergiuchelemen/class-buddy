package com.demo.ClassBuddy.security;

import com.demo.ClassBuddy.exception.UnauthorizedException;
import com.demo.ClassBuddy.service.JwtTokenService;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Service
public class JwtAuthFilter extends OncePerRequestFilter {
    private final CustomAuthenticationEntryPoint entryPoint;
    private final JwtTokenService jwtTokenService;
    private final UserDetailsService userDetailsService;

    public JwtAuthFilter(CustomAuthenticationEntryPoint entryPoint, JwtTokenService jwtTokenService, UserDetailsService userDetailsService) {
        this.entryPoint = entryPoint;
        this.jwtTokenService = jwtTokenService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer")) {
            if (isSecuredEndpoint(request)) {
                entryPoint.commence(request, response, new UnauthorizedException("Token is missing, invalid or expired."));
            } else {
                filterChain.doFilter(request, response);
            }
            return;
        }

        try {
            String jwtToken = authHeader.substring(7);
            String userEmail = jwtTokenService.extractEmail(jwtToken);
            authenticateUser(jwtToken, userEmail, request);
        } catch (IndexOutOfBoundsException | JwtException e) {
            entryPoint.commence(request, response, new UnauthorizedException("Token is missing, invalid or expired."));
            return;
        }
        filterChain.doFilter(request, response);
    }

    private void authenticateUser(String token, String userEmail, HttpServletRequest request) {
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            return;
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);
        if (jwtTokenService.isTokenValid(token, userDetails)) {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, null);
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }
    }

    private boolean isSecuredEndpoint(HttpServletRequest request) {
        return !request.getRequestURI().equals("/register") && !request.getRequestURI().equals("/login");
    }
}
