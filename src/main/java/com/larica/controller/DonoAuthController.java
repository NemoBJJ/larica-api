package com.larica.controller;

import com.larica.entity.DonoRestaurante;
import com.larica.repository.DonoRestauranteRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth/donos")
public class DonoAuthController {
    
    private final DonoRestauranteRepository donoRepository;

    public DonoAuthController(DonoRestauranteRepository donoRepository) {
        this.donoRepository = donoRepository;
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
}