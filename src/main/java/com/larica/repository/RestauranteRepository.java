// src/main/java/com/larica/repository/RestauranteRepository.java
package com.larica.repository;

import com.larica.entity.Restaurante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
// import java.util.Optional;  // não é mais usado aqui

public interface RestauranteRepository extends JpaRepository<Restaurante, Long> {

    // REMOVIDO: findByDonoRestauranteId(Long donoId) porque não existe coluna/relacionamento no BD.

    @Query("SELECT r FROM Restaurante r WHERE LOWER(r.nome) LIKE LOWER(CONCAT('%', :nome, '%'))")
    List<Restaurante> findByNomeContainingIgnoreCase(String nome);
}
