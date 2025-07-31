package com.larica.controller;

import com.larica.entity.ItemPedido;
import com.larica.service.ItemPedidoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/itens-pedido")
public class ItemPedidoController {
    private final ItemPedidoService itemPedidoService;

    public ItemPedidoController(ItemPedidoService itemPedidoService) {
        this.itemPedidoService = itemPedidoService;
    }

    @GetMapping("/pedido/{pedidoId}")
    public ResponseEntity<List<ItemPedido>> listarPorPedido(@PathVariable Long pedidoId) {
        return ResponseEntity.ok(itemPedidoService.listarPorPedido(pedidoId));
    }

    @PostMapping
    public ResponseEntity<ItemPedido> criar(@RequestBody ItemPedido itemPedido) {
        return ResponseEntity.ok(itemPedidoService.salvar(itemPedido));
    }

    @PostMapping("/lista")
    public ResponseEntity<List<ItemPedido>> criarVarios(@RequestBody List<ItemPedido> itens) {
        return ResponseEntity.ok(itemPedidoService.salvarTodos(itens));
    }
}