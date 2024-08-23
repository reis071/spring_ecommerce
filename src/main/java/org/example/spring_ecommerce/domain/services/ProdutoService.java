package org.example.spring_ecommerce.domain.services;


import org.example.spring_ecommerce.domain.entities.Produto;
import org.example.spring_ecommerce.domain.repositories.ProdutoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProdutoService {

    private ProdutoRepository produtoRepository;

    public ProdutoService(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    public Produto save(Produto produto) {
            if (produtoRepository.findByNome(produto.getNome()).isPresent()) {
                throw new IllegalArgumentException("Já existe um produto com esse nome.");
            }

            produto.setCriadoEm(LocalDateTime.now());
            produto.setAtualizadoEm(LocalDateTime.now());
            return produtoRepository.save(produto);
    }

    public Produto findById(Long id) {
        return produtoRepository.findById(id).orElseThrow(() -> new RuntimeException("Produto não encontrado"));
    }

    public List< Produto> findAll() {
        return produtoRepository.findAll();
    }

    public void deleteById(Long id) {
        Produto produto = findById(id);
        if (!produto.getItensVenda().isEmpty()) {

            produto.setAtivo(false);
            produtoRepository.save(produto);
        } else {
            produtoRepository.deleteById(id);
        }
    }
}
