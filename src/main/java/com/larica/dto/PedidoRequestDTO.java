package com.larica.dto;

import java.util.List;

public class PedidoRequestDTO {
    private Long usuarioId;
    private Long restauranteId;
    private List<ItemPedidoEntradaDTO> itens;

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public Long getRestauranteId() {
        return restauranteId;
    }

    public void setRestauranteId(Long restauranteId) {
        this.restauranteId = restauranteId;
    }

    public List<ItemPedidoEntradaDTO> getItens() {
        return itens;
    }

    public void setItens(List<ItemPedidoEntradaDTO> itens) {
        this.itens = itens;
    }
}
