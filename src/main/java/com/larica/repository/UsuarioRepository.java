package com.larica.repository;

import com.larica.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    boolean existsByEmail(String email);
    Optional<Usuario> findByEmail(String email);
    List<Usuario> findByTipo(String tipo);
    
    // Métodos padrão do JpaRepository já incluem:
    // save(), deleteById(), findAll(), findById(), etc.
}