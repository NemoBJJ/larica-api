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
    List<Pedido> findByClienteId(Long clienteId);
    Page<Pedido> findByClienteId(Long clienteId, Pageable pageable);
    
    @Query("SELECT p FROM Pedido p WHERE p.cliente.id = :clienteId ORDER BY p.data DESC")
    Optional<Pedido> findFirstByClienteIdOrderByDataDesc(Long clienteId);
}