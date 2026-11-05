package com.eryckavel.service;

import com.eryckavel.dto.request.DespesaRequest;
import com.eryckavel.dto.response.DespesaResponse;
import com.eryckavel.mapper.DespesaMapper;
import com.eryckavel.model.Despesa;
import com.eryckavel.model.Usuario;
import com.eryckavel.repository.DespesaRepository;
import com.eryckavel.repository.UsuarioRepository;
import com.eryckavel.security.UsuarioLogado;
import jakarta.persistence.EntityNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DespesaService {

    private static final Logger log = LogManager.getLogger(DespesaService.class);

    private final DespesaRepository despesaRepository;
    private final UsuarioRepository usuarioRepository;
    private final DespesaMapper despesaMapper;

    public DespesaService(DespesaRepository despesaRepository,
                           UsuarioRepository usuarioRepository,
                           DespesaMapper despesaMapper) {
        this.despesaRepository = despesaRepository;
        this.usuarioRepository = usuarioRepository;
        this.despesaMapper = despesaMapper;
    }

    @Transactional(readOnly = true)
    public List<DespesaResponse> listar() {
        Long idUsuario = UsuarioLogado.obterId();
        log.info("Listando despesas do usuário. idUsuario: {}", idUsuario);
        return despesaRepository.findByUsuarioId(idUsuario).stream().map(despesaMapper::toResponse).toList();
    }

    @Transactional
    public DespesaResponse cadastrar(DespesaRequest request) {
        Long idUsuario = UsuarioLogado.obterId();
        log.info("Cadastrando despesa. idUsuario: {}", idUsuario);
        log.info("Cadastrando despesa.request: {}", request);
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> {
                    log.warn("Erro ao cadastrar despesa: usuário não encontrado. idUsuario: {}", idUsuario);
                    return new EntityNotFoundException("Usuário não encontrado.");
                });
        Despesa despesa = despesaMapper.toEntity(request, usuario);
        return despesaMapper.toResponse(despesaRepository.save(despesa));
    }

    @Transactional(readOnly = true)
    public DespesaResponse buscarPorId(Long id) {
        Long idUsuario = UsuarioLogado.obterId();
        log.info("Buscando despesa por id. id: {}, idUsuario: {}", id, idUsuario);
        Despesa despesa = despesaRepository.findByIdAndUsuarioId(id, idUsuario)
                .orElseThrow(() -> {
                    log.warn("Erro ao buscar despesa: despesa não encontrada. id: {}, idUsuario: {}", id, idUsuario);
                    return new EntityNotFoundException("Despesa não encontrada.");
                });
        return despesaMapper.toResponse(despesa);
    }

    @Transactional
    public DespesaResponse atualizar(Long id, DespesaRequest request) {
        Long idUsuario = UsuarioLogado.obterId();
        log.info("Atualizando despesa. id: {}, idUsuario: {}", id, idUsuario);
        Despesa despesa = despesaRepository.findByIdAndUsuarioId(id, idUsuario)
                .orElseThrow(() -> {
                    log.warn("Erro ao tentar atualizar despesa: despesa não encontrada. id: {}, idUsuario: {}", id, idUsuario);
                    return new EntityNotFoundException("Despesa não encontrada.");
                });
        despesaMapper.updateEntity(request, despesa);
        return despesaMapper.toResponse(despesaRepository.save(despesa));
    }

    @Transactional
    public void deletar(Long id) {
        Long idUsuario = UsuarioLogado.obterId();
        log.info("Deletando despesa. id: {}, idUsuario: {}", id, idUsuario);
        Despesa despesa = despesaRepository.findByIdAndUsuarioId(id, idUsuario)
                .orElseThrow(() -> {
                    log.warn("Erro ao tentar deletar despesa: despesa não encontrada. id: {}, idUsuario: {}", id, idUsuario);
                    return new EntityNotFoundException("Despesa não encontrada.");
                });
        despesaRepository.delete(despesa);
    }

}