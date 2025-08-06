package com.larica.controller;

import com.larica.entity.DonoRestaurante;
import com.larica.entity.Restaurante;
import com.larica.repository.DonoRestauranteRepository;
import com.larica.repository.RestauranteRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth/donos")
public class DonoAuthController {

    private final DonoRestauranteRepository donoRepository;
    private final RestauranteRepository restauranteRepository;

    public DonoAuthController(DonoRestauranteRepository donoRepository,
                               RestauranteRepository restauranteRepository) {
        this.donoRepository = donoRepository;
        this.restauranteRepository = restauranteRepository;
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
        return ResponseEntity.ok(Map.of(
                "id", dono.getId(),
                "nome", dono.getNome(),
                "email", dono.getEmail(),
                "telefone", dono.getTelefone()
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
        restaurante.setDonoRestaurante(novoDono); // JPA já cuida da FK
        restauranteRepository.save(restaurante);

        return ResponseEntity.ok("Dono e restaurante cadastrados com sucesso");
    }
}
