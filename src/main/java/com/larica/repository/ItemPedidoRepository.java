package com.larica.repository;

import com.larica.entity.ItemPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Repository
public interface ItemPedidoRepository extends JpaRepository<ItemPedido, Long> {
    List<ItemPedido> findByPedidoId(Long pedidoId);
    
    // Adicione este método se precisar carregar o produto junto
    @Query("SELECT i FROM ItemPedido i JOIN FETCH i.produto WHERE i.pedido.id = :pedidoId")
    List<ItemPedido> findByPedidoIdWithProduto(@Param("pedidoId") Long pedidoId);
}