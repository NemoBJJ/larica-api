package com.larica.controller;

import com.larica.dto.ProdutoDTO;
import com.larica.entity.Produto;
import com.larica.entity.Restaurante;
import com.larica.mapper.ProdutoMapper;
import com.larica.service.ProdutoService;
import com.larica.service.RestauranteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/produtos")
public class ProdutoController {

    private final ProdutoService produtoService;
    private final RestauranteService restauranteService;

    public ProdutoController(ProdutoService produtoService,
                             RestauranteService restauranteService) {
        this.produtoService = produtoService;
        this.restauranteService = restauranteService;
    }

    // EXISTENTE: lista produtos por restaurante
    @GetMapping("/por-restaurante/{restauranteId}")
    public ResponseEntity<List<ProdutoDTO>> listarPorRestaurante(@PathVariable Long restauranteId) {
        List<Produto> produtos = produtoService.listarPorRestaurante(restauranteId);
        List<ProdutoDTO> dtos = produtos.stream()
                .map(ProdutoMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    // NOVO: cria produto (nome, descrição, preço) para um restaurante
    @PostMapping("/por-restaurante/{restauranteId}")
    public ResponseEntity<ProdutoDTO> criarProduto(@PathVariable Long restauranteId,
                                                   @RequestBody ProdutoDTO dto) {
        Restaurante restaurante = restauranteService.buscarPorId(restauranteId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Restaurante não encontrado"));

        Produto novo = new Produto();
        novo.setNome(dto.getNome());
        novo.setDescricao(dto.getDescricao());
        novo.setPreco(dto.getPreco());
        // sem campo 'disponivel' aqui
        novo.setRestaurante(restaurante);

        Produto salvo = produtoService.salvar(novo);
        return ResponseEntity.status(HttpStatus.CREATED).body(ProdutoMapper.toDTO(salvo));
    }
}
