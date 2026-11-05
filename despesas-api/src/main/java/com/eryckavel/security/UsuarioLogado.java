package com.eryckavel.security;

import org.springframework.security.core.context.SecurityContextHolder;

public final class UsuarioLogado {

    private UsuarioLogado() {
    }

    public static UsuarioAutenticado obter() {
        return (UsuarioAutenticado) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public static Long obterId() {
        return obter().getId();
    }
}