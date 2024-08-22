package org.example.spring_ecommerce.domain.services;


import org.example.spring_ecommerce.domain.entities.Produto;
import org.example.spring_ecommerce.domain.repositories.ProdutoRepository;
import org.springframework.stereotype.Service;

@Service
public class ProdutoService {

    private ProdutoRepository produtoRepository;

    public ProdutoService(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    public Produto save(Produto produto) {
        return produtoRepository.save(produto);
    }
}
