package com.larica.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "pedidos")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario cliente;

    @ManyToOne
    @JoinColumn(name = "restaurante_id")
    private Restaurante restaurante;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL)
    private List<ItemPedido> itens;

    private LocalDateTime data;
    private String status;

    // Construtores
    public Pedido() {
    }

    public Pedido(Usuario cliente, Restaurante restaurante, List<ItemPedido> itens, LocalDateTime data, String status) {
        this.cliente = cliente;
        this.restaurante = restaurante;
        this.itens = itens;
        this.data = data;
        this.status = status;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public Usuario getCliente() {
        return cliente;
    }

    public Restaurante getRestaurante() {
        return restaurante;
    }

    public List<ItemPedido> getItens() {
        return itens;
    }

    public LocalDateTime getData() {
        return data;
    }

    public String getStatus() {
        return status;
    }

    public BigDecimal getTotal() {
        if (itens == null) return BigDecimal.ZERO;

        return itens.stream()
                .map(item -> item.getProduto().getPreco().multiply(BigDecimal.valueOf(item.getQuantidade())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setCliente(Usuario cliente) {
        this.cliente = cliente;
    }

    public void setRestaurante(Restaurante restaurante) {
        this.restaurante = restaurante;
    }

    public void setItens(List<ItemPedido> itens) {
        this.itens = itens;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
