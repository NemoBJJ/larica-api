package com.larica.controller;

import com.larica.dto.*;
import com.larica.entity.Restaurante;
import com.larica.entity.Usuario;
import com.larica.mapper.*;
import com.larica.service.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/restaurantes")
public class RestauranteController {
    private final RestauranteService restauranteService;
    private final UsuarioService usuarioService;
    private final RestauranteMapper restauranteMapper;
    private final UsuarioMapper usuarioMapper;

    public RestauranteController(RestauranteService restauranteService,
                              UsuarioService usuarioService,
                              RestauranteMapper restauranteMapper,
                              UsuarioMapper usuarioMapper) {
        this.restauranteService = restauranteService;
        this.usuarioService = usuarioService;
        this.restauranteMapper = restauranteMapper;
        this.usuarioMapper = usuarioMapper;
    }

    // ========== ENDPOINTS DE RESTAURANTE ==========
    @PostMapping
    public ResponseEntity<RestauranteDTO> criarRestaurante(@RequestBody Restaurante restaurante) {
        Restaurante salvo = restauranteService.salvar(restaurante);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(salvo.getId())
                .toUri();
        return ResponseEntity.created(location).body(restauranteMapper.toDTO(salvo));
    }

    @PostMapping("/com-dono")
    public ResponseEntity<RestauranteComDonoDTO> criarRestauranteComDono(@RequestBody RestauranteCompletoDTO dto) {
        Restaurante restaurante = restauranteService.cadastrarRestauranteComDono(dto);
        return ResponseEntity.ok(restauranteMapper.toComDonoDTO(restaurante));
    }

    @GetMapping
    public ResponseEntity<List<RestauranteDTO>> listarRestaurantes() {
        List<Restaurante> restaurantes = restauranteService.listarTodos();
        return ResponseEntity.ok(restaurantes.stream()
                .map(restauranteMapper::toDTO)
                .collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestauranteDTO> buscarRestaurante(@PathVariable Long id) {
        return ResponseEntity.ok(restauranteMapper.toDTO(
            restauranteService.buscarPorId(id).orElseThrow()
        ));
    }

    @GetMapping("/por-dono/{donoId}")
    public ResponseEntity<RestauranteDTO> buscarPorDono(@PathVariable Long donoId) {
        return ResponseEntity.ok(restauranteMapper.toDTO(
            restauranteService.buscarPorDonoId(donoId)
        ));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarRestaurante(@PathVariable Long id) {
        restauranteService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    // ========== ENDPOINTS DE USUÁRIO/DONO ==========
    @PostMapping("/donos")
    public ResponseEntity<UsuarioDTO> criarDono(@RequestBody UsuarioDTO dto) {
        Usuario salvo = usuarioService.salvar(usuarioMapper.toEntity(dto));
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(salvo.getId())
                .toUri();
        return ResponseEntity.created(location).body(usuarioMapper.toDTO(salvo));
    }

    @GetMapping("/donos/{id}")
    public ResponseEntity<UsuarioDTO> buscarDono(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioMapper.toDTO(
            usuarioService.buscarPorId(id).orElseThrow()
        ));
    }

    @GetMapping("/donos")
    public ResponseEntity<List<UsuarioDTO>> listarDonos() {
        List<Usuario> donos = usuarioService.listarPorTipo("DONO");
        return ResponseEntity.ok(donos.stream()
                .map(usuarioMapper::toDTO)
                .collect(Collectors.toList()));
    }

    @PutMapping("/donos/{id}")
    public ResponseEntity<UsuarioDTO> atualizarDono(@PathVariable Long id, @RequestBody UsuarioDTO dto) {
        Usuario usuario = usuarioMapper.toEntity(dto);
        usuario.setId(id);
        return ResponseEntity.ok(usuarioMapper.toDTO(usuarioService.salvar(usuario)));
    }

    @DeleteMapping("/donos/{id}")
    public ResponseEntity<Void> deletarDono(@PathVariable Long id) {
        usuarioService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}