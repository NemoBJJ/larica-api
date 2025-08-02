package com.larica.repository;

import com.larica.entity.DonoRestaurante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.Optional;

public interface DonoRestauranteRepository extends JpaRepository<DonoRestaurante, Long> {
    
    @Query("SELECT d FROM DonoRestaurante d JOIN d.usuario u WHERE u.email = :email")
    Optional<DonoRestaurante> findByUsuarioEmail(String email);
    
    @Query("SELECT CASE WHEN COUNT(d) > 0 THEN true ELSE false END FROM DonoRestaurante d JOIN d.usuario u WHERE u.email = :email")
    boolean existsByUsuarioEmail(String email);
}