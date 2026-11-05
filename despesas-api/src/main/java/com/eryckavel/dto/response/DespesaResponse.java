package com.eryckavel.dto.response;

import com.eryckavel.model.enums.CategoriaDespesa;

import java.time.LocalDate;

public record DespesaResponse(
    Long id,
    String descricao,
    Double valor,
    LocalDate data,
    CategoriaDespesa categoria,
    String nomeUsuario
) {
}