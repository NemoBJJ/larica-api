package com.larica.controller;

import com.larica.entity.Usuario;
import com.larica.repository.UsuarioRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioRepository usuarioRepository;

    public UsuarioController(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    // Cadastrar usuário (POST /usuarios)
    @PostMapping
    public ResponseEntity<Usuario> cadastrarUsuario(@RequestBody Usuario usuario) {
        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        
        usuario.setDataCadastro(LocalDate.now());
        Usuario usuarioSalvo = usuarioRepository.save(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioSalvo);
    }

    // Listar todos os usuários (GET /usuarios)
    @GetMapping
    public ResponseEntity<List<Usuario>> listarTodos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<Usuario> usuarios = usuarioRepository.findAll(pageable);
        return ResponseEntity.ok(usuarios.getContent());
    }

    // Buscar usuário por ID (GET /usuarios/{id})
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> buscarPorId(@PathVariable Long id) {
        Optional<Usuario> usuario = usuarioRepository.findById(id);
        return usuario.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
    }

    // Atualizar usuário (PUT /usuarios/{id})
    @PutMapping("/{id}")
    public ResponseEntity<Usuario> atualizarUsuario(
            @PathVariable Long id,
            @RequestBody Usuario usuarioAtualizado) {
        
        if (!usuarioRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        
        usuarioAtualizado.setId(id);
        usuarioAtualizado.setDataCadastro(usuarioRepository.findById(id).get().getDataCadastro());
        Usuario usuario = usuarioRepository.save(usuarioAtualizado);
        return ResponseEntity.ok(usuario);
    }

    // Deletar usuário (DELETE /usuarios/{id})
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarUsuario(@PathVariable Long id) {
        if (!usuarioRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        
        usuarioRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // Verificar se email existe (GET /usuarios/existe-email?email=teste@teste.com)
    @GetMapping("/existe-email")
    public ResponseEntity<Boolean> verificarEmailExistente(
            @RequestParam String email) {
        
        boolean existe = usuarioRepository.existsByEmail(email);
        return ResponseEntity.ok(existe);
    }
} 