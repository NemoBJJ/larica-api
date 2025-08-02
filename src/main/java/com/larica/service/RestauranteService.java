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
    private final UsuarioRepository usuarioRepository;

    public RestauranteService(RestauranteRepository restauranteRepository,
                            DonoRestauranteRepository donoRestauranteRepository,
                            UsuarioRepository usuarioRepository) {
        this.restauranteRepository = restauranteRepository;
        this.donoRestauranteRepository = donoRestauranteRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public Restaurante cadastrarRestauranteComDono(RestauranteCompletoDTO dto) {
        // Validação
        if (dto == null || dto.getDono() == null) {
            throw new IllegalArgumentException("Dados do restaurante e dono são obrigatórios");
        }

        // Cria usuário dono
        Usuario usuario = new Usuario();
        usuario.setNome(dto.getDono().getNome());
        usuario.setEmail(dto.getDono().getEmail());
        usuario.setSenha(dto.getDono().getSenha());
        usuario.setTipo("DONO");
        usuario.setDataCadastro(LocalDate.now());
        
        usuario = usuarioRepository.save(usuario);
        
        // Cria relação dono-restaurante
        DonoRestaurante dono = new DonoRestaurante();
        dono.setUsuario(usuario);
        dono = donoRestauranteRepository.save(dono);
        
        // Cria restaurante
        Restaurante restaurante = new Restaurante();
        restaurante.setNome(dto.getNome());
        restaurante.setEndereco(dto.getEndereco());
        restaurante.setTelefone(dto.getTelefone());
        restaurante.setDonoRestaurante(dono);
        
        return restauranteRepository.save(restaurante);
    }

    // Outros métodos permanecem iguais
    public Restaurante salvar(Restaurante restaurante) {
        return restauranteRepository.save(restaurante);
    }

    public Optional<Restaurante> buscarPorId(Long id) {
        return restauranteRepository.findById(id);
    }

    public Restaurante buscarPorDonoId(Long donoId) {
        return restauranteRepository.findByDonoRestauranteUsuarioId(donoId)
                .orElseThrow(() -> new RuntimeException("Restaurante não encontrado"));
    }

    public void deletar(Long id) {
        restauranteRepository.deleteById(id);
    }

    public List<Restaurante> listarTodos() {
        return restauranteRepository.findAll();
    }
}