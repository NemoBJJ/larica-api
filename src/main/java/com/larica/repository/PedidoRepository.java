package com.larica.repository;

import com.larica.entity.Pedido;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    // Métodos existentes
    List<Pedido> findByClienteId(Long clienteId);
    Optional<Pedido> findFirstByClienteIdOrderByDataDesc(Long clienteId);
    
    // Novos métodos para restaurante
    Page<Pedido> findByRestauranteId(Long restauranteId, Pageable pageable);
    
    @Query("SELECT p FROM Pedido p WHERE p.id = :pedidoId AND p.restaurante.id = :restauranteId")
    Optional<Pedido> findByIdAndRestauranteId(Long pedidoId, Long restauranteId);
}