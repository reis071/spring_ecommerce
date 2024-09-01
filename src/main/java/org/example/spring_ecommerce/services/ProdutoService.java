package org.example.spring_ecommerce.services;


import org.example.spring_ecommerce.model.Produto;
import org.example.spring_ecommerce.repositories.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    public ProdutoService(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    //adiciona produto
    @CacheEvict(value = "produtoCache", allEntries = true)
    public Produto save(Produto produto) {
            if (produtoRepository.findByNome(produto.getNome()).isPresent()) {
                throw new IllegalArgumentException("Já existe um produto com esse nome.");
            }

            produto.setCriadoEm(LocalDateTime.now());
            produto.setAtualizadoEm(LocalDateTime.now());
            return produtoRepository.save(produto);
    }

    //retorna produto por id
    @Cacheable("produtoCache")
    public Produto findById(Long id) {
        return produtoRepository.findById(id).orElseThrow(() -> new RuntimeException("Produto não encontrado"));
    }

    //retorna todos os produtos
    @Cacheable("produtoCache")
    public List< Produto> findAll() {
        return produtoRepository.findAll();
    }

    //deleta ou inativa o produto
    @Cacheable("produtoCache")
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
