package com.larica.dto;

import java.time.LocalDate;
import java.util.List;

public class HistoricoPedidoDTO {
    private Long pedidoId;
    private String nomeRestaurante;
    private LocalDate data;
    private String status;
    private List<ItemPedidoDTO> itens;

    public HistoricoPedidoDTO(Long pedidoId, String nomeRestaurante, LocalDate data, String status, List<ItemPedidoDTO> itens) {
        this.pedidoId = pedidoId;
        this.nomeRestaurante = nomeRestaurante;
        this.data = data;
        this.status = status;
        this.itens = itens;
    }

    // Getters
    public Long getPedidoId() { return pedidoId; }
    public String getNomeRestaurante() { return nomeRestaurante; }
    public LocalDate getData() { return data; }
    public String getStatus() { return status; }
    public List<ItemPedidoDTO> getItens() { return itens; }
}