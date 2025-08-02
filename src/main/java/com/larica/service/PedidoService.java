package com.larica.service;

import com.larica.dto.*;
import com.larica.entity.*;
import com.larica.repository.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.larica.dto.PedidoRestauranteDTO.*;

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

    // ========== MÉTODOS EXISTENTES (USUÁRIO) ==========
    
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

        itens.forEach(item -> {
            item.setPedido(pedidoSalvo);
            itemPedidoRepository.save(item);
        });

        return pedidoSalvo;
    }

    public List<HistoricoPedidoDTO> listarHistoricoPorUsuario(Long usuarioId) {
        return pedidoRepository.findByClienteId(usuarioId).stream()
            .map(this::convertToHistoricoDTO)
            .collect(Collectors.toList());
    }

    public HistoricoPedidoDTO buscarUltimoPedidoDTO(Long usuarioId) {
        Optional<Pedido> ultimoPedido = pedidoRepository.findFirstByClienteIdOrderByDataDesc(usuarioId);
        return ultimoPedido.map(this::convertToHistoricoDTO).orElse(null);
    }

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

    private HistoricoPedidoDTO convertToHistoricoDTO(Pedido pedido) {
        return new HistoricoPedidoDTO(
            pedido.getId(),
            pedido.getRestaurante() != null ? pedido.getRestaurante().getNome() : "Restaurante não informado",
            pedido.getData().toLocalDate(),
            pedido.getStatus(),
            listarItensPorPedido(pedido.getId())
        );
    }

    // ========== NOVOS MÉTODOS (RESTAURANTE) ==========
    
    public Page<PedidoRestauranteDTO> listarPedidosRestaurante(Long restauranteId, Pageable pageable) {
        Page<Pedido> pedidos = pedidoRepository.findByRestauranteId(restauranteId, pageable);
        return pedidos.map(this::converterParaRestauranteDTO);
    }

    public PedidoRestauranteDTO buscarPedidoRestaurante(Long restauranteId, Long pedidoId) {
        Pedido pedido = pedidoRepository.findByIdAndRestauranteId(pedidoId, restauranteId)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));
        return converterParaRestauranteDTO(pedido);
    }

    public PedidoRestauranteDTO atualizarStatusPedido(Long restauranteId, Long pedidoId, String novoStatus) {
        Pedido pedido = pedidoRepository.findByIdAndRestauranteId(pedidoId, restauranteId)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));
        
        pedido.setStatus(novoStatus);
        Pedido pedidoAtualizado = pedidoRepository.save(pedido);
        return converterParaRestauranteDTO(pedidoAtualizado);
    }

    private PedidoRestauranteDTO converterParaRestauranteDTO(Pedido pedido) {
        List<ItemPedidoDTO> itensDTO = listarItensPorPedido(pedido.getId());
        
        Double total = itensDTO.stream()
                .mapToDouble(item -> item.getPrecoUnitario().doubleValue() * item.getQuantidade())
                .sum();

        return new PedidoRestauranteDTO(
                pedido.getId(),
                pedido.getData(),
                pedido.getStatus(),
                pedido.getCliente().getNome(),
                pedido.getCliente().getTelefone(),
                itensDTO,
                total
        );
    }
}