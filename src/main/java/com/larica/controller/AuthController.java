package com.larica.controller;

import com.larica.entity.Usuario;
import com.larica.dto.RegisterRequestDTO;
import com.larica.repository.UsuarioRepository;
import com.larica.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;
    private final UsuarioRepository usuarioRepository;

    public AuthController(AuthService authService, UsuarioRepository usuarioRepository) {
        this.authService = authService;
        this.usuarioRepository = usuarioRepository;
    }

    @PostMapping("/registro")
    public Usuario registrar(@RequestBody Usuario usuario) {
        return authService.registrarUsuario(usuario);
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequestDTO request) {
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            return ResponseEntity.badRequest().body("E-mail já cadastrado");
        }

        Usuario novoUsuario = new Usuario();
        novoUsuario.setNome(request.getNome());
        novoUsuario.setEmail(request.getEmail());
        novoUsuario.setSenha(request.getSenha()); // sem criptografia por enquanto

        usuarioRepository.save(novoUsuario);

        return ResponseEntity.ok("Usuário cadastrado com sucesso");
    }
}
