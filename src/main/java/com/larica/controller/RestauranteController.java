package com.larica.controller;

import com.larica.dto.*;
import com.larica.entity.Restaurante;
import com.larica.entity.Usuario;
import com.larica.mapper.*;
import com.larica.service.GeoService;
import com.larica.service.RestauranteService;
import com.larica.service.UsuarioService;
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
    private final GeoService geoService;

    public RestauranteController(RestauranteService restauranteService,
                                 UsuarioService usuarioService,
                                 RestauranteMapper restauranteMapper,
                                 UsuarioMapper usuarioMapper,
                                 GeoService geoService) {
        this.restauranteService = restauranteService;
        this.usuarioService = usuarioService;
        this.restauranteMapper = restauranteMapper;
        this.usuarioMapper = usuarioMapper;
        this.geoService = geoService;
    }

    @PostMapping
    public ResponseEntity<RestauranteDTO> criarRestaurante(@RequestBody Restaurante restaurante) {
        if (restaurante.getEndereco() != null && (restaurante.getLatitude() == null || restaurante.getLongitude() == null)) {
            GeoService.Coordenadas coordenadas = geoService.obterCoordenadasPorEndereco(restaurante.getEndereco());
            restaurante.setLatitude(coordenadas.getLatitude());
            restaurante.setLongitude(coordenadas.getLongitude());
        }

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
        if (dto.getEndereco() != null && (dto.getLatitude() == null || dto.getLongitude() == null)) {
            GeoService.Coordenadas coordenadas = geoService.obterCoordenadasPorEndereco(dto.getEndereco());
            dto.setLatitude(coordenadas.getLatitude());
            dto.setLongitude(coordenadas.getLongitude());
        }

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

    @GetMapping("/proximos")
    public ResponseEntity<List<RestauranteDTO>> listarProximos(
            @RequestParam Double lat,
            @RequestParam Double lng,
            @RequestParam(defaultValue = "5") Double raioKm) {

        List<Restaurante> proximos = restauranteService.buscarPorProximidade(lat, lng, raioKm);
        List<RestauranteDTO> dtos = proximos.stream()
                .map(restauranteMapper::toDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }
}
