package com.larica.controller;

import com.larica.dto.PedidoRestauranteDTO;
import com.larica.service.PedidoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/restaurantes/{restauranteId}/pedidos")
public class RestaurantePedidoController {

    private final PedidoService pedidoService;

    public RestaurantePedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @GetMapping
    public ResponseEntity<Page<PedidoRestauranteDTO>> listarPedidos(
            @PathVariable Long restauranteId,
            Pageable pageable) {
        return ResponseEntity.ok(pedidoService.listarPedidosRestaurante(restauranteId, pageable));
    }

    @GetMapping("/{pedidoId}")
    public ResponseEntity<PedidoRestauranteDTO> buscarPedido(
            @PathVariable Long restauranteId,
            @PathVariable Long pedidoId) {
        return ResponseEntity.ok(pedidoService.buscarPedidoRestaurante(restauranteId, pedidoId));
    }

    @PatchMapping("/{pedidoId}/status")
    public ResponseEntity<PedidoRestauranteDTO> atualizarStatus(
            @PathVariable Long restauranteId,
            @PathVariable Long pedidoId,
            @RequestParam String status) {
        return ResponseEntity.ok(pedidoService.atualizarStatusPedido(restauranteId, pedidoId, status));
    }
}