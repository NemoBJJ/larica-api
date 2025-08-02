package com.larica.service;

import com.larica.entity.Usuario;
import com.larica.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    // Método salvarUsuario (já existente)
    public Usuario salvarUsuario(Usuario usuario) {
        if (usuario.getDataCadastro() == null) {
            usuario.setDataCadastro(LocalDate.now());
        }
        return usuarioRepository.save(usuario);
    }

    // Adicione este método para compatibilidade
    public Usuario salvar(Usuario usuario) {
        return salvarUsuario(usuario);
    }

    // Restante dos métodos permanece igual
    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> buscarPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    public void deletar(Long id) {
        usuarioRepository.deleteById(id);
    }

    public boolean existePorEmail(String email) {
        return usuarioRepository.existsByEmail(email);
    }

    public List<Usuario> listarPorTipo(String tipo) {
        return usuarioRepository.findByTipo(tipo);
    }

    public Optional<Usuario> buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }
}