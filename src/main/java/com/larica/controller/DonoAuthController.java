package com.larica.controller;

import com.larica.entity.DonoRestaurante;
import com.larica.entity.Restaurante;
import com.larica.repository.DonoRestauranteRepository;
import com.larica.repository.RestauranteRepository;
import com.larica.service.GeoService;
import com.larica.security.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;
import java.util.List;

@RestController
@RequestMapping("/auth/donos")
public class DonoAuthController {

    private final DonoRestauranteRepository donoRepository;
    private final RestauranteRepository restauranteRepository;
    private final GeoService geoService;
    private final JwtUtil jwtUtil;

    public DonoAuthController(DonoRestauranteRepository donoRepository,
                             RestauranteRepository restauranteRepository,
                             GeoService geoService,
                             JwtUtil jwtUtil) {
        this.donoRepository = donoRepository;
        this.restauranteRepository = restauranteRepository;
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

        // GERAR TOKEN JWT PARA O DONO
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
        // Dados do dono
        String nomeDono = payload.get("nome");
        String emailDono = payload.get("email");
        String senhaDono = payload.get("senha");
        String telefoneDono = payload.get("telefone");

        // Dados do restaurante
        String nomeRestaurante = payload.get("nomeRestaurante");
        String enderecoRestaurante = payload.get("enderecoRestaurante");
        String telefoneRestaurante = payload.get("telefoneRestaurante");

        if (donoRepository.existsByEmail(emailDono)) {
            return ResponseEntity.badRequest().body("E-mail já cadastrado");
        }

        // 1️⃣ Salva o dono
        DonoRestaurante novoDono = new DonoRestaurante();
        novoDono.setNome(nomeDono);
        novoDono.setEmail(emailDono);
        novoDono.setSenha(senhaDono);
        novoDono.setTelefone(telefoneDono);
        novoDono.setDataCadastro(LocalDate.now());
        donoRepository.save(novoDono);

        // 2️⃣ Cria e salva o restaurante vinculado ao dono
        Restaurante restaurante = new Restaurante();
        restaurante.setNome(nomeRestaurante);
        restaurante.setEndereco(enderecoRestaurante);
        restaurante.setTelefone(telefoneRestaurante);
        restaurante.setDonoRestaurante(novoDono);

        // 3️⃣ Busca latitude e longitude via GeoService
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
}
