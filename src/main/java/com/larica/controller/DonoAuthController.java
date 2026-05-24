package com.larica.controller;

import com.larica.entity.DonoRestaurante;
import com.larica.entity.Pedido;
import com.larica.entity.Restaurante;
import com.larica.repository.DonoRestauranteRepository;
import com.larica.repository.PedidoRepository;
import com.larica.repository.RestauranteRepository;
import com.larica.service.GeoService;
import com.larica.security.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.List;

@RestController
@RequestMapping("/auth/donos")
public class DonoAuthController {

    private final DonoRestauranteRepository donoRepository;
    private final RestauranteRepository restauranteRepository;
    private final PedidoRepository pedidoRepository;
    private final GeoService geoService;
    private final JwtUtil jwtUtil;

    public DonoAuthController(DonoRestauranteRepository donoRepository,
                             RestauranteRepository restauranteRepository,
                             PedidoRepository pedidoRepository,
                             GeoService geoService,
                             JwtUtil jwtUtil) {
        this.donoRepository = donoRepository;
        this.restauranteRepository = restauranteRepository;
        this.pedidoRepository = pedidoRepository;
        this.geoService = geoService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginDono(@RequestBody Map<String, String> credentials) {
        String email = credentials.get("email");
        String senha = credentials.get("senha");

        Optional<DonoRestaurante> donoOpt = donoRepository.findByEmail(email);

        if (donoOpt.isEmpty() || !donoOpt.get().getSenha().equals(senha)) {
            return ResponseEntity.status(401).body("Credenciais inválidas");
        }

        DonoRestaurante dono = donoOpt.get();

        String token = jwtUtil.generateToken(
            dono.getEmail(),
            dono.getId(),
            List.of("DONO")
        );

        return ResponseEntity.ok(Map.of(
            "id", dono.getId(),
            "nome", dono.getNome(),
            "email", dono.getEmail(),
            "telefone", dono.getTelefone(),
            "token", token
        ));
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerDonoComRestaurante(@RequestBody Map<String, String> payload) {
        String nomeDono = payload.get("nome");
        String emailDono = payload.get("email");
        String senhaDono = payload.get("senha");
        String telefoneDono = payload.get("telefone");

        String nomeRestaurante = payload.get("nomeRestaurante");
        String enderecoRestaurante = payload.get("enderecoRestaurante");
        String telefoneRestaurante = payload.get("telefoneRestaurante");

        if (donoRepository.existsByEmail(emailDono)) {
            return ResponseEntity.badRequest().body("E-mail já cadastrado");
        }

        DonoRestaurante novoDono = new DonoRestaurante();
        novoDono.setNome(nomeDono);
        novoDono.setEmail(emailDono);
        novoDono.setSenha(senhaDono);
        novoDono.setTelefone(telefoneDono);
        novoDono.setDataCadastro(LocalDate.now());
        donoRepository.save(novoDono);

        Restaurante restaurante = new Restaurante();
        restaurante.setNome(nomeRestaurante);
        restaurante.setEndereco(enderecoRestaurante);
        restaurante.setTelefone(telefoneRestaurante);
        restaurante.setDonoRestaurante(novoDono);

        try {
            GeoService.Coordenadas coords = geoService.obterCoordenadasPorEndereco(enderecoRestaurante);
            restaurante.setLatitude(coords.getLatitude());
            restaurante.setLongitude(coords.getLongitude());
        } catch (Exception e) {
            System.err.println("Erro ao obter coordenadas: " + e.getMessage());
        }

        restauranteRepository.save(restaurante);

        return ResponseEntity.ok("Dono e restaurante cadastrados com sucesso");
    }

    @GetMapping("/entregador/pedido/{pedidoId}/rota")
    public ResponseEntity<?> getRotaEntregador(@PathVariable Long pedidoId) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
            .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));

        Map<String, Object> rota = new HashMap<>();
        rota.put("pedidoId", pedido.getId());
        rota.put("status", pedido.getStatus());
        
        // Dados do restaurante
        rota.put("latRestaurante", pedido.getRestaurante().getLatitude());
        rota.put("lngRestaurante", pedido.getRestaurante().getLongitude());
        rota.put("enderecoRestaurante", pedido.getRestaurante().getEndereco());
        
        // ✅ FALLBACK REMOVIDO! Agora retorna null se não tiver localização
        // O frontend vai usar o localStorage para preencher o endereço do cliente
        rota.put("enderecoCliente", null);
        rota.put("latCliente", null);
        rota.put("lngCliente", null);

        return ResponseEntity.ok(rota);
    }
}