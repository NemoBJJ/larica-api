// src/main/java/com/larica/service/RestauranteService.java
package com.larica.service;

import com.larica.dto.RestauranteCompletoDTO;
import com.larica.entity.*;
import com.larica.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class RestauranteService {
    private final RestauranteRepository restauranteRepository;
    private final DonoRestauranteRepository donoRestauranteRepository;

    public RestauranteService(RestauranteRepository restauranteRepository,
                              DonoRestauranteRepository donoRestauranteRepository) {
        this.restauranteRepository = restauranteRepository;
        this.donoRestauranteRepository = donoRestauranteRepository;
    }

    @Transactional
    public Restaurante cadastrarRestauranteComDono(RestauranteCompletoDTO dto) {
        if (dto == null || dto.getDono() == null) {
            throw new IllegalArgumentException("Dados do restaurante e dono são obrigatórios");
        }

        DonoRestaurante dono = new DonoRestaurante();
        dono.setNome(dto.getDono().getNome());
        dono.setEmail(dto.getDono().getEmail());
        dono.setSenha(dto.getDono().getSenha());
        dono.setTelefone(dto.getDono().getTelefone());
        dono.setDataCadastro(LocalDate.now());
        dono = donoRestauranteRepository.save(dono);
        
        Restaurante restaurante = new Restaurante();
        restaurante.setNome(dto.getNome());
        restaurante.setEndereco(dto.getEndereco());
        restaurante.setTelefone(dto.getTelefone());
        // Sem relação persistida no BD: somente em memória
        restaurante.setDonoRestaurante(dono);
        
        return restauranteRepository.save(restaurante);
    }

    public Restaurante salvar(Restaurante restaurante) {
        return restauranteRepository.save(restaurante);
    }

    public Optional<Restaurante> buscarPorId(Long id) {
        return restauranteRepository.findById(id);
    }

    // DESATIVADO: não há coluna/relacionamento persistente para dono no BD.
    public Restaurante buscarPorDonoId(Long donoId) {
        throw new UnsupportedOperationException("Busca por dono desativada: restaurante não possui coluna/relacionamento de dono no BD.");
    }

    public void deletar(Long id) {
        restauranteRepository.deleteById(id);
    }

    public List<Restaurante> listarTodos() {
        return restauranteRepository.findAll();
    }
}
