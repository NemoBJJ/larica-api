package com.larica.repository;

import com.larica.entity.Restaurante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;

public interface RestauranteRepository extends JpaRepository<Restaurante, Long> {
    
    @Query("SELECT r FROM Restaurante r JOIN r.donoRestaurante d JOIN d.usuario u WHERE u.id = :usuarioId")
    Optional<Restaurante> findByDonoRestauranteUsuarioId(Long usuarioId);
    
    // Método corrigido para busca por nome
    @Query("SELECT r FROM Restaurante r WHERE LOWER(r.nome) LIKE LOWER(CONCAT('%', :nome, '%'))")
    List<Restaurante> findByNomeContainingIgnoreCase(String nome);
}