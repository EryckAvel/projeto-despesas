package com.eryckavel.controller;

import com.eryckavel.dto.request.CadastroUsuarioRequest;
import com.eryckavel.dto.request.LoginRequest;
import com.eryckavel.dto.request.ResetSenhaUsuarioRequest;
import com.eryckavel.dto.response.CadastroUsuarioResponse;
import com.eryckavel.dto.response.LoginResponse;
import com.eryckavel.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/cadastro")
    public ResponseEntity<CadastroUsuarioResponse> cadastrar(@Valid @RequestBody CadastroUsuarioRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.cadastrar(request));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(authService.login(request));
    }

    @PutMapping("/alterar-senha")
    public ResponseEntity<Void> alterarSenha(@Valid @RequestBody ResetSenhaUsuarioRequest request) {
        authService.alterarSenha(request);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}