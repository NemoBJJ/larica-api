package com.larica.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "itens_pedido")
public class ItemPedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "produto_id")
    private Produto produto;
    
    private int quantidade;
    
    @ManyToOne
    @JoinColumn(name = "pedido_id")
    private Pedido pedido;

    // Construtores
    public ItemPedido() {
    }

    public ItemPedido(Produto produto, int quantidade, Pedido pedido) {
        this.produto = produto;
        this.quantidade = quantidade;
        this.pedido = pedido;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public Produto getProduto() {
        return produto;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public Pedido getPedido() {
        return pedido;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }
}