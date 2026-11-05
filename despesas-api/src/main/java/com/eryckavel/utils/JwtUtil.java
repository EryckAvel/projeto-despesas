package com.eryckavel.utils;

import com.eryckavel.security.UsuarioAutenticado;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;

@Component
public class JwtUtil {

    private final SecretKey chave;
    private final Duration expiracao;

    public JwtUtil(@Value("${jwt.secret}") String secret,
                   @Value("${jwt.expiration}") Duration expiracao) {
        this.chave = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
        this.expiracao = expiracao;
    }

    public String gerarToken(UsuarioAutenticado usuario) {
        Instant agora = Instant.now();
        return Jwts.builder()
                .subject(usuario.getUsername())
                .claim("idUsuario", usuario.getId())
                .issuedAt(Date.from(agora))
                .expiration(Date.from(agora.plus(expiracao)))
                .signWith(chave)
                .compact();
    }

    public String extrairUsername(String token) {
        return extrairClaims(token).getSubject();
    }

    public Long extrairIdUsuario(String token) {
        return extrairClaims(token).get("idUsuario", Long.class);
    }

    public Date extrairExpiracao(String token) {
        return extrairClaims(token).getExpiration();
    }

    public boolean tokenValido(String token, UserDetails usuario) {
        return extrairUsername(token).equals(usuario.getUsername()) && !tokenExpirado(token);
    }

    private boolean tokenExpirado(String token) {
        return extrairExpiracao(token).before(new Date());
    }

    private Claims extrairClaims(String token) {
        return Jwts.parser()
                .verifyWith(chave)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}