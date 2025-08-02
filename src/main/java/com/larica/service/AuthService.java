package com.larica.service;

import com.larica.entity.Usuario;
import com.larica.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDate;

@Service
public class AuthService {
    private final UsuarioRepository usuarioRepository;

    public AuthService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public Usuario registrarUsuario(Usuario usuario) {
        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            throw new RuntimeException("Email já cadastrado!");
        }
        usuario.setDataCadastro(LocalDate.now());
        return usuarioRepository.save(usuario);
    }

    public boolean login(String email, String senha) {
        return usuarioRepository.findByEmail(email)
            .map(usuario -> usuario.getSenha().equals(senha))
            .orElse(false);
    }
}