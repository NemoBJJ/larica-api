package com.larica.dto;

import java.math.BigDecimal;

public class ItemPedidoDTO {
    private Long id;
    private String nomeProduto;
    private int quantidade;
    private BigDecimal precoUnitario;

    // Construtor
    public ItemPedidoDTO(Long id, String nomeProduto, int quantidade, BigDecimal precoUnitario) {
        this.id = id;
        this.nomeProduto = nomeProduto;
        this.quantidade = quantidade;
        this.precoUnitario = precoUnitario;
    }

    // Getters (obrigatórios para serialização JSON)
    public Long getId() {
        return id;
    }

    public String getNomeProduto() {
        return nomeProduto;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public BigDecimal getPrecoUnitario() {
        return precoUnitario;
    }
}
