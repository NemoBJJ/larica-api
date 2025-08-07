package com.larica.controller;

import com.larica.dto.*;
import com.larica.entity.ItemPedido;
import com.larica.entity.Pedido;
import com.larica.entity.Produto;
import com.larica.repository.ProdutoRepository;
import com.larica.service.PedidoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;
    private final ProdutoRepository produtoRepository;

    public PedidoController(PedidoService pedidoService, ProdutoRepository produtoRepository) {
        this.pedidoService = pedidoService;
        this.produtoRepository = produtoRepository;
    }

    @PostMapping
    public ResponseEntity<PedidoCriadoDTO> criarPedido(@RequestBody PedidoRequestDTO dto) {
        List<ItemPedido> itensConvertidos = dto.getItens().stream().map(i -> {
            Produto produto = produtoRepository.findById(i.getProdutoId())
                    .orElseThrow(() -> new RuntimeException("Produto não encontrado com ID: " + i.getProdutoId()));
            ItemPedido item = new ItemPedido();
            item.setProduto(produto);
            item.setQuantidade(i.getQuantidade());
            return item;
        }).collect(Collectors.toList());

        Pedido novoPedido = pedidoService.criarPedido(
                dto.getUsuarioId(),
                dto.getRestauranteId(),
                itensConvertidos
        );

        PedidoCriadoDTO body = new PedidoCriadoDTO(
                novoPedido.getId(),
                novoPedido.getStatus(),
                novoPedido.getData()
        );
        return ResponseEntity.ok(body);
    }

    @GetMapping("/cliente/{usuarioId}")
    public ResponseEntity<List<HistoricoPedidoDTO>> buscarHistoricoPorUsuario(@PathVariable Long usuarioId) {
        List<HistoricoPedidoDTO> historico = pedidoService.listarHistoricoPorUsuario(usuarioId);
        return ResponseEntity.ok(historico);
    }

    @GetMapping("/restaurante/{restauranteId}")
    public ResponseEntity<List<HistoricoPedidoDTO>> buscarHistoricoPorRestaurante(@PathVariable Long restauranteId) {
        List<HistoricoPedidoDTO> historico = pedidoService.listarHistoricoPorRestaurante(restauranteId);
        return ResponseEntity.ok(historico);
    }

    @GetMapping("/ultimo/{usuarioId}")
    public ResponseEntity<HistoricoPedidoDTO> buscarUltimoPedido(@PathVariable Long usuarioId) {
        HistoricoPedidoDTO ultimoPedido = pedidoService.buscarUltimoPedidoDTO(usuarioId);
        return ultimoPedido != null ? ResponseEntity.ok(ultimoPedido) : ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}/itens")
    public ResponseEntity<List<ItemPedidoDTO>> listarItensPorPedido(@PathVariable Long id) {
        List<ItemPedidoDTO> itens = pedidoService.listarItensPorPedido(id);
        return itens.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(itens);
    }

    @PatchMapping("/restaurante/{restauranteId}/{pedidoId}/status")
    @CrossOrigin(origins = "http://localhost:3000", methods = RequestMethod.PATCH)
    public ResponseEntity<PedidoRestauranteDTO> atualizarStatusPedido(
            @PathVariable Long restauranteId,
            @PathVariable Long pedidoId,
            @RequestParam String status) {

        PedidoRestauranteDTO atualizado = pedidoService.atualizarStatusPedido(restauranteId, pedidoId, status);
        return ResponseEntity.ok(atualizado);
    }

    // 🔥 NOVO: Histórico geral (todos os pedidos, ordenado por data desc)
    @GetMapping("/todos")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<List<HistoricoPedidoDTO>> listarHistoricoGeral() {
        List<HistoricoPedidoDTO> historico = pedidoService.listarHistoricoGeral();
        return ResponseEntity.ok(historico);
    }
}
