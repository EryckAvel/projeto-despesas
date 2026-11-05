package com.eryckavel.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CadastroUsuarioRequest(
        @NotBlank(message = "O nome é obrigatório")
        String nome,
        @NotBlank(message = "O e-mail é obrigatório")
        String email,
        @NotBlank(message = "A senha é obrigatória")
        String senha
) {
}