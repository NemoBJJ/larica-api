package com.larica.controller;

import com.larica.dto.RestauranteDTO;
import com.larica.entity.Restaurante;
import com.larica.mapper.RestauranteMapper;
import com.larica.service.RestauranteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/restaurantes")
public class RestauranteController {

    private final RestauranteService restauranteService;

    public RestauranteController(RestauranteService restauranteService) {
        this.restauranteService = restauranteService;
    }

    @PostMapping
    public ResponseEntity<RestauranteDTO> criar(@RequestBody Restaurante restaurante) {
        Restaurante salvo = restauranteService.salvar(restaurante);
        RestauranteDTO dto = RestauranteMapper.toDTO(salvo);
        return ResponseEntity.ok(dto);
    }

    @GetMapping
    public ResponseEntity<List<RestauranteDTO>> listar(
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "10") int tamanho) {
        List<Restaurante> restaurantes = restauranteService.listarTodos(pagina, tamanho);
        List<RestauranteDTO> dtos = restaurantes.stream()
                .map(RestauranteMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestauranteDTO> buscar(@PathVariable Long id) {
        Optional<Restaurante> restaurante = restauranteService.buscarPorId(id);
        return restaurante.map(r -> ResponseEntity.ok(RestauranteMapper.toDTO(r)))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        restauranteService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
