package com.larica.mapper;

import com.larica.dto.ProdutoDTO;
import com.larica.entity.Produto;

public class ProdutoMapper {

    public static ProdutoDTO toDTO(Produto produto) {
        if (produto == null) return null;

        ProdutoDTO dto = new ProdutoDTO(
            produto.getId(),
            produto.getNome(),
            produto.getDescricao(),
            produto.getPreco(),
            produto.getImagemUrl()  // 🔥 ADICIONADO
        );
        
        return dto;
    }

    public static Produto toEntity(ProdutoDTO dto) {
        if (dto == null) return null;

        Produto produto = new Produto();
        produto.setId(dto.getId());
        produto.setNome(dto.getNome());
        produto.setDescricao(dto.getDescricao());
        produto.setPreco(dto.getPreco());
        produto.setImagemUrl(dto.getImagemUrl());  // 🔥 ADICIONADO

        return produto;
    }
}