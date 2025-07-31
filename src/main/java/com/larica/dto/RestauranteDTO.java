package com.larica.dto;

import java.util.List;

public class RestauranteDTO {
    private Long id;
    private String nome;
    private String endereco;
    private String telefone;
    private List<ProdutoDTO> cardapio;

    public RestauranteDTO() {
    }

    public RestauranteDTO(Long id, String nome, String endereco, String telefone, List<ProdutoDTO> cardapio) {
        this.id = id;
        this.nome = nome;
        this.endereco = endereco;
        this.telefone = telefone;
        this.cardapio = cardapio;
    }

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

    public List<ProdutoDTO> getCardapio() {
        return cardapio;
    }

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

    public void setCardapio(List<ProdutoDTO> cardapio) {
        this.cardapio = cardapio;
    }
}
