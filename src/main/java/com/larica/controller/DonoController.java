package com.larica.controller;

import com.larica.dto.RestauranteComDonoDTO;
import com.larica.dto.RestauranteCompletoDTO;
import com.larica.service.RestauranteService;
import com.larica.mapper.RestauranteMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/donos")
public class DonoController {

    private final RestauranteService restauranteService;
    private final RestauranteMapper restauranteMapper;

    public DonoController(RestauranteService restauranteService, 
                         RestauranteMapper restauranteMapper) {
        this.restauranteService = restauranteService;
        this.restauranteMapper = restauranteMapper;
    }

    @PostMapping("/cadastrar-restaurante")
    public ResponseEntity<RestauranteComDonoDTO> cadastrarRestaurante(
            @RequestBody RestauranteCompletoDTO dto) {
        return ResponseEntity.ok(
            restauranteMapper.toComDonoDTO(restauranteService.cadastrarRestauranteComDono(dto))
        );
    }

    @GetMapping("/{donoId}/restaurante")
    public ResponseEntity<RestauranteComDonoDTO> buscarRestaurantePorDono(
            @PathVariable Long donoId) {
        return ResponseEntity.ok(
            restauranteMapper.toComDonoDTO(restauranteService.buscarPorDonoId(donoId))
        );
    }
}