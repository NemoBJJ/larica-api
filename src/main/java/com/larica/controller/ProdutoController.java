package com.larica.controller;

import com.larica.dto.ProdutoDTO;
import com.larica.entity.Produto;
import com.larica.mapper.ProdutoMapper;
import com.larica.service.ProdutoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    private final ProdutoService produtoService;

    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    @GetMapping("/por-restaurante/{restauranteId}")
    public ResponseEntity<List<ProdutoDTO>> listarPorRestaurante(@PathVariable Long restauranteId) {
        List<Produto> produtos = produtoService.listarPorRestaurante(restauranteId);
        List<ProdutoDTO> dtos = produtos.stream()
                                        .map(ProdutoMapper::toDTO)
                                        .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }
}
