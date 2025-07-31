package com.larica.repository;

import com.larica.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    
    // Verifica se um email já está cadastrado
    boolean existsByEmail(String email);
    
    // Busca um usuário pelo email (adicionei para o AuthService)
    Optional<Usuario> findByEmail(String email);
}