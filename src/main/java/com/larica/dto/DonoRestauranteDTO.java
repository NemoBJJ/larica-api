package com.larica.dto;

import com.larica.dto.UsuarioDTO;

public class DonoRestauranteDTO {
    private Long id;
    private UsuarioDTO usuario;

    // Construtores
    public DonoRestauranteDTO() {
    }

    public DonoRestauranteDTO(Long id, UsuarioDTO usuario) {
        this.id = id;
        this.usuario = usuario;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UsuarioDTO getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioDTO usuario) {
        this.usuario = usuario;
    }
}