package com.eryckavel.controller;

import com.eryckavel.dto.request.DespesaRequest;
import com.eryckavel.dto.response.DespesaResponse;
import com.eryckavel.service.DespesaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/despesa")
public class DespesaController {

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