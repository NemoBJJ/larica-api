package com.larica.controller;

import com.larica.entity.Usuario;
import com.larica.dto.RegisterRequestDTO;
import com.larica.repository.UsuarioRepository;
import com.larica.service.AuthService;
import com.larica.security.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.Optional;
import java.util.List;

@RestController
@RequestMapping("/auth/usuarios")
public class AuthController {

    private final AuthService authService;
    private final UsuarioRepository usuarioRepository;
    private final JwtUtil jwtUtil;

    public AuthController(AuthService authService, UsuarioRepository usuarioRepository, JwtUtil jwtUtil) {
        this.authService = authService;
        this.usuarioRepository = usuarioRepository;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUsuario(@RequestBody Map<String, String> credentials) {
        String email = credentials.get("email");
        String senha = credentials.get("senha");
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);

        if (usuarioOpt.isPresent() && usuarioOpt.get().getSenha().equals(senha)) {
            Usuario usuario = usuarioOpt.get();

            String token = jwtUtil.generateToken(
                usuario.getEmail(),
                usuario.getId(),
                List.of("CLIENTE")
            );

            return ResponseEntity.ok().body(Map.of(
                "id", usuario.getId(),
                "nome", usuario.getNome(),
                "email", usuario.getEmail(),
                "token", token
            ));
        }
        return ResponseEntity.status(401).body("Credenciais inválidas");
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
        novoUsuario.setSenha(request.getSenha());
        usuarioRepository.save(novoUsuario);
        return ResponseEntity.ok("Usuário cadastrado com sucesso");
    }
}
