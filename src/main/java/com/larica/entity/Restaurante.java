package com.larica.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "restaurantes")
public class Restaurante {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String nome;
    
    @Column(nullable = false)
    private String endereco;
    
    private String telefone;
    
    @OneToOne
    @JoinColumn(name = "dono_restaurante_id", nullable = false)
    private DonoRestaurante donoRestaurante;
    
    @OneToMany(mappedBy = "restaurante", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Produto> cardapio = new ArrayList<>();

    public Restaurante() {
    }

    public Restaurante(String nome, String endereco, String telefone, DonoRestaurante donoRestaurante) {
        this.nome = nome;
        this.endereco = endereco;
        this.telefone = telefone;
        this.donoRestaurante = donoRestaurante;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public DonoRestaurante getDonoRestaurante() {
        return donoRestaurante;
    }

    public void setDonoRestaurante(DonoRestaurante donoRestaurante) {
        this.donoRestaurante = donoRestaurante;
    }

    public List<Produto> getCardapio() {
        return cardapio;
    }

    public void setCardapio(List<Produto> cardapio) {
        this.cardapio = cardapio;
    }

    public void adicionarProduto(Produto produto) {
        cardapio.add(produto);
        produto.setRestaurante(this);
    }

    public void removerProduto(Produto produto) {
        cardapio.remove(produto);
        produto.setRestaurante(null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Restaurante that = (Restaurante) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Restaurante{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", dono=" + (donoRestaurante != null ? donoRestaurante.getUsuario().getNome() : "null") +
                '}';
    }
}