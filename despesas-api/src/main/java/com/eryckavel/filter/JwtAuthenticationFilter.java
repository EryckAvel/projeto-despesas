package com.eryckavel.filter;

import com.eryckavel.security.UsuarioAutenticado;
import com.eryckavel.service.AuthService;
import com.eryckavel.utils.JwtUtil;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String PREFIXO_BEARER = "Bearer ";

    private final JwtUtil jwtUtil;
    private final AuthService authService;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, AuthService authService) {
        this.jwtUtil = jwtUtil;
        this.authService = authService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (header == null || !header.startsWith(PREFIXO_BEARER)) {
            filterChain.doFilter(request, response);
            return;
        }
        String token = header.substring(PREFIXO_BEARER.length());
        try {
            String username = jwtUtil.extrairUsername(token);
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails usuario = authService.loadUserByUsername(username);
                if (jwtUtil.tokenValido(token, usuario)) {
                    Long idUsuario = jwtUtil.extrairIdUsuario(token);
                    UsuarioAutenticado usuarioAutenticado = new UsuarioAutenticado(idUsuario, usuario.getUsername(), usuario.getPassword());
                    UsernamePasswordAuthenticationToken autenticacao = new UsernamePasswordAuthenticationToken(usuarioAutenticado, null, usuario.getAuthorities());
                    autenticacao.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(autenticacao);
                }
            }
        } catch (JwtException | IllegalArgumentException | UsernameNotFoundException e) {
            SecurityContextHolder.clearContext();
        }
        filterChain.doFilter(request, response);
    }
}