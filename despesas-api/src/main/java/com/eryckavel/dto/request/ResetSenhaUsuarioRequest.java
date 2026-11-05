package com.eryckavel.dto.request;

import jakarta.validation.constraints.NotBlank;

public record ResetSenhaUsuarioRequest (
        @NotBlank String email,
        @NotBlank String novaSenha
) {
}
