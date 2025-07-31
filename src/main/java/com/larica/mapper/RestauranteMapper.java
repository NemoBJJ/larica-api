package com.larica.mapper;

import com.larica.dto.ProdutoDTO;
import com.larica.dto.RestauranteDTO;
import com.larica.entity.Produto;
import com.larica.entity.Restaurante;

import java.util.List;
import java.util.stream.Collectors;

public class RestauranteMapper {

    public static RestauranteDTO toDTO(Restaurante restaurante) {
        List<ProdutoDTO> cardapio = restaurante.getCardapio().stream()
            .map(RestauranteMapper::toProdutoDTO)
            .collect(Collectors.toList());

        return new RestauranteDTO(
            restaurante.getId(),
            restaurante.getNome(),
            restaurante.getEndereco(),
            restaurante.getTelefone(),
            cardapio
        );
    }

    private static ProdutoDTO toProdutoDTO(Produto produto) {
        return new ProdutoDTO(
            produto.getId(),
            produto.getNome(),
            produto.getDescricao(),
            produto.getPreco()
        );
    }
}
