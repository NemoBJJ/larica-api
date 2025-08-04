package com.larica.repository;

import com.larica.entity.DonoRestaurante;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface DonoRestauranteRepository extends JpaRepository<DonoRestaurante, Long> {
    Optional<DonoRestaurante> findByEmail(String email);
    boolean existsByEmail(String email);
}