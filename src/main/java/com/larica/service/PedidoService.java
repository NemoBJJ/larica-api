package com.larica.service;

import com.larica.dto.*;
import com.larica.entity.*;
import com.larica.repository.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ItemPedidoRepository itemPedidoRepository;
    private final UsuarioRepository usuarioRepository;
    private final RestauranteRepository restauranteRepository;

    public PedidoService(
        PedidoRepository pedidoRepository,
        ItemPedidoRepository itemPedidoRepository,
        UsuarioRepository usuarioRepository,
        RestauranteRepository restauranteRepository
    ) {
        this.pedidoRepository = pedidoRepository;
        this.itemPedidoRepository = itemPedidoRepository;
        this.usuarioRepository = usuarioRepository;
        this.restauranteRepository = restauranteRepository;
    }

    // Método usado pelo seu controller (não mudei)
    public Pedido criarPedido(Long usuarioId, Long restauranteId, List<ItemPedido> itens) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        Restaurante restaurante = restauranteRepository.findById(restauranteId)
            .orElseThrow(() -> new RuntimeException("Restaurante não encontrado"));

        Pedido pedido = new Pedido();
        pedido.setCliente(usuario);
        pedido.setRestaurante(restaurante);
        pedido.setData(LocalDateTime.now());
        pedido.setStatus("AGUARDANDO");

        Pedido pedidoSalvo = pedidoRepository.save(pedido);

        // Associa os itens ao pedido
        itens.forEach(item -> {
            item.setPedido(pedidoSalvo);
            itemPedidoRepository.save(item);
        });

        return pedidoSalvo;
    }

    // Método usado pelo seu controller (não mudei)
    public List<HistoricoPedidoDTO> listarHistoricoPorUsuario(Long usuarioId) {
        return pedidoRepository.findByClienteId(usuarioId).stream()
            .map(this::convertToHistoricoDTO)
            .collect(Collectors.toList());
    }

    // Método usado pelo seu controller (não mudei)
    public HistoricoPedidoDTO buscarUltimoPedidoDTO(Long usuarioId) {
        Optional<Pedido> ultimoPedido = pedidoRepository.findFirstByClienteIdOrderByDataDesc(usuarioId);
        return ultimoPedido.map(this::convertToHistoricoDTO).orElse(null);
    }

    // Método usado pelo seu controller (AJUSTEI AQUI)
    public List<ItemPedidoDTO> listarItensPorPedido(Long pedidoId) {
        List<ItemPedido> itens = itemPedidoRepository.findByPedidoIdWithProduto(pedidoId);
        
        return itens.stream()
            .map(item -> {
                if (item.getProduto() == null) {
                    return new ItemPedidoDTO(
                        item.getId(),
                        "Produto indisponível",
                        item.getQuantidade(),
                        BigDecimal.ZERO
                    );
                }
                return new ItemPedidoDTO(
                    item.getId(),
                    item.getProduto().getNome(),
                    item.getQuantidade(),
                    item.getProduto().getPreco()
                );
            })
            .collect(Collectors.toList());
    }

    // Método interno (não mudei)
    private HistoricoPedidoDTO convertToHistoricoDTO(Pedido pedido) {
        return new HistoricoPedidoDTO(
            pedido.getId(),
            pedido.getRestaurante() != null ? pedido.getRestaurante().getNome() : "Restaurante não informado",
            pedido.getData().toLocalDate(),
            pedido.getStatus(),
            listarItensPorPedido(pedido.getId())
        );
    }
}