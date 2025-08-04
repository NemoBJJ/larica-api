package com.larica.dto;

public class RestauranteComDonoDTO {
    private Long id;
    private String nome;
    private String endereco;
    private String telefone;
    private DonoRestauranteDTO dono;

    // Getters e Setters
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

    public DonoRestauranteDTO getDono() {
        return dono;
    }

    public void setDono(DonoRestauranteDTO dono) {
        this.dono = dono;
    }
}