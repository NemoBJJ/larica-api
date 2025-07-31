package com.larica.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "restaurantes")
public class Restaurante {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String nome;
    private String endereco;
    private String telefone;
    
    @OneToMany(mappedBy = "restaurante")
    private List<Produto> cardapio;

    // Construtores
    public Restaurante() {
    }

    public Restaurante(String nome, String endereco, String telefone) {
        this.nome = nome;
        this.endereco = endereco;
        this.telefone = telefone;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getEndereco() {
        return endereco;
    }

    public String getTelefone() {
        return telefone;
    }

    public List<Produto> getCardapio() {
        return cardapio;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public void setCardapio(List<Produto> cardapio) {
        this.cardapio = cardapio;
    }
}