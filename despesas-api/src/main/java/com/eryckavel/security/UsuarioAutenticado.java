package com.eryckavel.security;

import com.eryckavel.model.Usuario;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class UsuarioAutenticado implements UserDetails {

    private final Long id;
    private final String email;
    private final String senha;

    public UsuarioAutenticado(Long id, String email, String senha) {
        this.id = id;
        this.email = email;
        this.senha = senha;
    }

    public static UsuarioAutenticado from(Usuario usuario) {
        return new UsuarioAutenticado(usuario.getId(), usuario.getEmail(), usuario.getSenha());
    }

    public Long getId() {
        return id;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return senha;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }
}