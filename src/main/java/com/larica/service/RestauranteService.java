// src/main/java/com/larica/service/RestauranteService.java
package com.larica.service;

import com.larica.dto.RestauranteCompletoDTO;
import com.larica.entity.DonoRestaurante;
import com.larica.entity.Restaurante;
import com.larica.repository.DonoRestauranteRepository;
import com.larica.repository.RestauranteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class RestauranteService {

    private final RestauranteRepository restauranteRepository;
    private final DonoRestauranteRepository donoRestauranteRepository;
    private final GeoService geoService;

    public RestauranteService(RestauranteRepository restauranteRepository,
                              DonoRestauranteRepository donoRestauranteRepository,
                              GeoService geoService) {
        this.restauranteRepository = restauranteRepository;
        this.donoRestauranteRepository = donoRestauranteRepository;
        this.geoService = geoService;
    }

    @Transactional
    public Restaurante cadastrarRestauranteComDono(RestauranteCompletoDTO dto) {
        if (dto == null || dto.getDono() == null) {
            throw new IllegalArgumentException("Dados do restaurante e dono são obrigatórios");
        }

        // 1. Cadastra dono
        DonoRestaurante dono = new DonoRestaurante();
        dono.setNome(dto.getDono().getNome());
        dono.setEmail(dto.getDono().getEmail());
        dono.setSenha(dto.getDono().getSenha());
        dono.setTelefone(dto.getDono().getTelefone());
        dono.setDataCadastro(LocalDate.now());
        dono = donoRestauranteRepository.save(dono);

        // 2. Cadastra restaurante
        Restaurante restaurante = new Restaurante();
        restaurante.setNome(dto.getNome());
        restaurante.setEndereco(dto.getEndereco());
        restaurante.setTelefone(dto.getTelefone());
        restaurante.setDonoRestaurante(dono);

        // 3. Se vier lat/lng do front, usa
        if (dto.getLatitude() != null && dto.getLongitude() != null) {
            restaurante.setLatitude(dto.getLatitude());
            restaurante.setLongitude(dto.getLongitude());
        } else {
            // 4. Se NÃO vier, chama a API do Google Geocoding pra buscar
            try {
                GeoService.Coordenadas coords = geoService.obterCoordenadasPorEndereco(dto.getEndereco());
                restaurante.setLatitude(coords.getLatitude());
                restaurante.setLongitude(coords.getLongitude());
            } catch (Exception e) {
                System.err.println("Erro ao buscar coordenadas: " + e.getMessage());
                // Se quiser, você pode lançar exceção aqui pra travar o cadastro
                // throw new RuntimeException("Erro ao buscar coordenadas");
            }
        }

        return restauranteRepository.save(restaurante);
    }

    public Restaurante salvar(Restaurante restaurante) {
        return restauranteRepository.save(restaurante);
    }

    public Optional<Restaurante> buscarPorId(Long id) {
        return restauranteRepository.findById(id);
    }

    public Restaurante buscarPorDonoId(Long donoId) {
        throw new UnsupportedOperationException("Busca por dono desativada: restaurante não possui coluna/relacionamento de dono no BD.");
    }

    public void deletar(Long id) {
        restauranteRepository.deleteById(id);
    }

    public List<Restaurante> listarTodos() {
        return restauranteRepository.findAll();
    }

    public List<Restaurante> buscarPorProximidade(Double latUser, Double lngUser, Double raioKm) {
        List<Restaurante> todos = restauranteRepository.findAll();

        return todos.stream()
                .filter(rest -> rest.getLatitude() != null && rest.getLongitude() != null)
                .filter(rest -> calcularDistanciaKm(latUser, lngUser, rest.getLatitude(), rest.getLongitude()) <= raioKm)
                .toList();
    }

    private double calcularDistanciaKm(double lat1, double lon1, double lat2, double lon2) {
        int R = 6371;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }
}
