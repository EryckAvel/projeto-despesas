package com.eryckavel.service;

import com.eryckavel.dto.request.CadastroUsuarioRequest;
import com.eryckavel.dto.request.LoginRequest;
import com.eryckavel.dto.request.ResetSenhaUsuarioRequest;
import com.eryckavel.dto.response.CadastroUsuarioResponse;
import com.eryckavel.dto.response.LoginResponse;
import com.eryckavel.exceptions.LoginException;
import com.eryckavel.model.Usuario;
import com.eryckavel.repository.UsuarioRepository;
import com.eryckavel.security.UsuarioAutenticado;
import com.eryckavel.utils.JwtUtil;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService implements UserDetailsService {

    private static final String MENSAGEM_LOGIN_INVALIDO = "E-mail ou senha inválidos";
    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado com o e-mail: " + email));
        return toUserDetails(usuario);
    }

    @Transactional
    public CadastroUsuarioResponse cadastrar(CadastroUsuarioRequest request) {
        if (usuarioRepository.existsByEmail(request.email())) {
            log.info("Erro ao cadastrar email ja cadastrado!!");
            throw new EntityExistsException("Email já cadastrado");
        }
        Usuario usuario = new Usuario();
        usuario.setNome(request.nome());
        usuario.setEmail(request.email());
        usuario.setSenha(passwordEncoder.encode(request.senha()));
        Usuario salvo = usuarioRepository.save(usuario);
        return new CadastroUsuarioResponse(salvo.getId(), salvo.getNome(), salvo.getEmail());
    }

    public LoginResponse login(LoginRequest request) {
        Usuario usuario = usuarioRepository.findByEmail(request.email())
                .orElseThrow(() -> new LoginException(MENSAGEM_LOGIN_INVALIDO));
        if (!passwordEncoder.matches(request.senha(), usuario.getSenha())) throw new LoginException(MENSAGEM_LOGIN_INVALIDO);
        return new LoginResponse(jwtUtil.gerarToken(UsuarioAutenticado.from(usuario)));
    }

    public String alterarSenha(ResetSenhaUsuarioRequest request) {
        Usuario usuario = usuarioRepository.findByEmail(request.email())
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado com esse e-mail!"));
        usuario.setSenha(passwordEncoder.encode(request.novaSenha()));
        usuarioRepository.save(usuario);
        return "Senha alterada com sucesso";
    }

    private UserDetails toUserDetails(Usuario usuario) {
        return UsuarioAutenticado.from(usuario);
    }

}