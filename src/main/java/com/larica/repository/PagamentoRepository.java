// src/main/java/com/larica/repository/PagamentoRepository.java
package com.larica.repository;

import com.larica.entity.Pagamento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PagamentoRepository extends JpaRepository<Pagamento, Long> {
    Optional<Pagamento> findByPreferenceId(String preferenceId);
    Optional<Pagamento> findByPedidoId(Long pedidoId);
}
