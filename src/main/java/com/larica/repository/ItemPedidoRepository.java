package com.larica.repository;

import com.larica.entity.ItemPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemPedidoRepository extends JpaRepository<ItemPedido, Long> {
    
    @Query("SELECT ip FROM ItemPedido ip JOIN FETCH ip.produto WHERE ip.pedido.id = :pedidoId")
    List<ItemPedido> findByPedidoIdWithProduto(@Param("pedidoId") Long pedidoId);
    
    List<ItemPedido> findByPedidoId(Long pedidoId);
}