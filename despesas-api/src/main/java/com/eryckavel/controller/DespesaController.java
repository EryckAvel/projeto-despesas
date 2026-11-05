package com.eryckavel.controller;

import com.eryckavel.dto.request.DespesaRequest;
import com.eryckavel.dto.response.DespesaResponse;
import com.eryckavel.service.DespesaService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/despesa")
public class DespesaController {

    private static final Logger log = LoggerFactory.getLogger(DespesaController.class);
    private final DespesaService despesaService;

    public DespesaController(DespesaService despesaService) {
        this.despesaService = despesaService;
    }

    @GetMapping
    public ResponseEntity<List<DespesaResponse>> listar() {
        return ResponseEntity.status(HttpStatus.OK).body(despesaService.listar());
    }

    @PostMapping
    public ResponseEntity<DespesaResponse> cadastrar(@Valid @RequestBody DespesaRequest request) {
        log.info("cadastrando despesa : {}", request.toString());
        return ResponseEntity.status(HttpStatus.CREATED).body(despesaService.cadastrar(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DespesaResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(despesaService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DespesaResponse> atualizar(@PathVariable Long id,
                                                     @Valid @RequestBody DespesaRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(despesaService.atualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        despesaService.deletar(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}