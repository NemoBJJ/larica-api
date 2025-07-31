package com.larica.service;

import com.larica.entity.Restaurante;
import com.larica.repository.RestauranteRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class RestauranteService {
    private final RestauranteRepository restauranteRepository;

    public RestauranteService(RestauranteRepository restauranteRepository) {
        this.restauranteRepository = restauranteRepository;
    }

    public Restaurante salvar(Restaurante restaurante) {
        return restauranteRepository.save(restaurante);
    }

    public Optional<Restaurante> buscarPorId(Long id) {
        return restauranteRepository.findById(id);
    }

    public List<Restaurante> listarTodos() {
        return restauranteRepository.findAll();
    }

    public List<Restaurante> listarTodos(int pagina, int tamanho) {
        PageRequest pageable = PageRequest.of(pagina, tamanho);
        return restauranteRepository.findAll(pageable).getContent();
    }

    public void deletar(Long id) {
        restauranteRepository.deleteById(id);
    }
}