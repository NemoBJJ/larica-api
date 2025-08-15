package com.larica.dto;

public class RestauranteCompletoDTO {

    private String nome;
    private String endereco;
    private String telefone;
    private Double latitude;
    private Double longitude;
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

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public UsuarioDTO getDono() {
        return dono;
    }

    public void setDono(UsuarioDTO dono) {
        this.dono = dono;
    }
}
