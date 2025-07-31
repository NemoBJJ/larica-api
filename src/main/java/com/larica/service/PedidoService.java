package com.larica.service;

import com.larica.dto.HistoricoPedidoDTO;
import com.larica.dto.ItemPedidoDTO;
import com.larica.entity.ItemPedido;
import com.larica.entity.Pedido;
import com.larica.entity.Restaurante;
import com.larica.entity.Usuario;
import com.larica.repository.ItemPedidoRepository;
import com.larica.repository.PedidoRepository;
import com.larica.repository.RestauranteRepository;
import com.larica.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PedidoService {
    private final PedidoRepository pedidoRepository;
    private final ItemPedidoRepository itemPedidoRepository;
    private final UsuarioRepository usuarioRepository;
    private final RestauranteRepository restauranteRepository;

    public PedidoService(PedidoRepository pedidoRepository, 
                        ItemPedidoRepository itemPedidoRepository,
                        UsuarioRepository usuarioRepository,
                        RestauranteRepository restauranteRepository) {
        this.pedidoRepository = pedidoRepository;
        this.itemPedidoRepository = itemPedidoRepository;
        this.usuarioRepository = usuarioRepository;
        this.restauranteRepository = restauranteRepository;
    }

    // Novo método para criar pedidos
    public Pedido criarPedido(Long usuarioId, Long restauranteId, List<ItemPedido> itens) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        Restaurante restaurante = restauranteRepository.findById(restauranteId)
                .orElseThrow(() -> new RuntimeException("Restaurante não encontrado"));
        
        Pedido pedido = new Pedido();
        pedido.setCliente(usuario);
        pedido.setRestaurante(restaurante);
        pedido.setItens(itens);
        pedido.setData(LocalDateTime.now());
        pedido.setStatus("AGUARDANDO");
        
        return pedidoRepository.save(pedido);
    }

    // Métodos existentes...
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
            .map(this::convertToItemPedidoDTO)
            .collect(Collectors.toList());
    }

    private ItemPedidoDTO convertToItemPedidoDTO(ItemPedido item) {
        if (item == null || item.getProduto() == null) {
            throw new IllegalArgumentException("ItemPedido ou Produto não pode ser nulo");
        }
        return new ItemPedidoDTO(
            item.getId(),
            item.getProduto().getNome(),
            item.getQuantidade(),
            item.getProduto().getPreco()
        );
    }

    private HistoricoPedidoDTO convertToHistoricoDTO(Pedido pedido) {
        if (pedido == null) {
            throw new IllegalArgumentException("Pedido não pode ser nulo");
        }
        return new HistoricoPedidoDTO(
            pedido.getId(),
            (pedido.getRestaurante() != null) ? pedido.getRestaurante().getNome() : "Restaurante não informado",
            pedido.getData().toLocalDate(),
            pedido.getStatus(),
            listarItensPorPedido(pedido.getId())
        );
    }
}