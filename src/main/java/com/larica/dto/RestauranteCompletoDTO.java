package com.larica.dto;

public class RestauranteCompletoDTO {
    private String nome;
    private String endereco;
    private String telefone;
    private UsuarioDTO dono;

    // Getters e Setters
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

    public UsuarioDTO getDono() {
        return dono;
    }

    public void setDono(UsuarioDTO dono) {
        this.dono = dono;
    }
}