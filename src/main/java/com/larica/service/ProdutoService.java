package com.larica.service;

import com.larica.entity.Produto;
import com.larica.repository.ProdutoRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProdutoService {
    private final ProdutoRepository produtoRepository;

    public ProdutoService(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    public Produto salvar(Produto produto) {
        return produtoRepository.save(produto);
    }

    public Optional<Produto> buscarPorId(Long id) {
        return produtoRepository.findById(id);
    }

    public List<Produto> listarPorRestaurante(Long restauranteId) {
        return produtoRepository.findByRestauranteId(restauranteId);
    }

    public List<Produto> listarPorRestaurante(Long restauranteId, int pagina, int tamanho) {
        return produtoRepository.findByRestauranteId(restauranteId, PageRequest.of(pagina, tamanho)).getContent();
    }

    public void deletar(Long id) {
        produtoRepository.deleteById(id);
    }
}
