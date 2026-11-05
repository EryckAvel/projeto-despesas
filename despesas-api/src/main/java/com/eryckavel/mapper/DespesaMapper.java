package com.eryckavel.mapper;

import com.eryckavel.dto.request.DespesaRequest;
import com.eryckavel.dto.response.DespesaResponse;
import com.eryckavel.model.Despesa;
import com.eryckavel.model.Usuario;
import org.springframework.stereotype.Component;

@Component
public class DespesaMapper {

    public Despesa toEntity(DespesaRequest request, Usuario usuario) {
        Despesa despesa = new Despesa();
        despesa.setDescricao(request.descricao());
        despesa.setValor(request.valor());
        despesa.setData(request.data());
        despesa.setCategoria(request.categoria());
        despesa.setUsuario(usuario);
        return despesa;
    }

    public void updateEntity(DespesaRequest request, Despesa despesa) {
        if (request.descricao() != null) despesa.setDescricao(request.descricao());
        if (request.valor() != null) despesa.setValor(request.valor());
        if (request.data() != null) despesa.setData(request.data());
        if (request.categoria() != null) despesa.setCategoria(request.categoria());
    }

    public DespesaResponse toResponse(Despesa despesa) {
        Usuario usuario = despesa.getUsuario();
        String nomeUsuario = usuario != null ? usuario.getNome() : null;
        return new DespesaResponse(
                despesa.getId(),
                despesa.getDescricao(),
                despesa.getValor(),
                despesa.getData(),
                despesa.getCategoria(),
                nomeUsuario
        );
    }
}