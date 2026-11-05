package com.eryckavel.dto.request;

import com.eryckavel.model.enums.CategoriaDespesa;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record DespesaRequest(
        @NotBlank(message = "A descrição é obrigatória.")
        @Size(max = 150, message = "A descrição deve ter no máximo 150 caracteres.")
        String descricao,
        @NotNull(message = "O valor é obrigatório.")
        @Digits(integer = 10, fraction = 2, message = "O valor deve ter no máximo 10 dígitos inteiros e 2 decimais.")
        Double valor,
        @NotNull(message = "A data é obrigatória.")
        LocalDate data,
        @NotNull(message = "A categoria é obrigatória.")
        CategoriaDespesa categoria
) {
}