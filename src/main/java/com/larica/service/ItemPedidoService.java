package com.larica.service;

import com.larica.entity.ItemPedido;
import com.larica.repository.ItemPedidoRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ItemPedidoService {
    private final ItemPedidoRepository itemPedidoRepository;

    public ItemPedidoService(ItemPedidoRepository itemPedidoRepository) {
        this.itemPedidoRepository = itemPedidoRepository;
    }

    public ItemPedido salvar(ItemPedido itemPedido) {
        return itemPedidoRepository.save(itemPedido);
    }

    public List<ItemPedido> salvarTodos(List<ItemPedido> itens) {
        return itemPedidoRepository.saveAll(itens);
    }

    public List<ItemPedido> listarPorPedido(Long pedidoId) {
        return itemPedidoRepository.findByPedidoId(pedidoId);
    }
}