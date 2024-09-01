package org.example.spring_ecommerce.controllers;

import org.example.spring_ecommerce.model.Produto;
import org.example.spring_ecommerce.services.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;


@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    @Autowired
    private ProdutoService produtoService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Produto> addProduto(@RequestBody Produto produto) {
        Produto novoProduto = produtoService.save(produto);

        return ResponseEntity.status(HttpStatus.CREATED).body(novoProduto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Produto> getProduto(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(produtoService.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<Produto>> getAllProdutos() {

        return ResponseEntity.status(HttpStatus.OK).body(produtoService.findAll());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Produto> updateProduto(@PathVariable Long id, @RequestBody Produto produto) {

        Produto existingProduto = produtoService.findById(id);

        if (existingProduto == null) {
            return ResponseEntity.notFound().build();
        }

        existingProduto.setNome(produto.getNome());
        existingProduto.setPreco(produto.getPreco());
        existingProduto.setAtivo(produto.isAtivo());
        existingProduto.setAtualizadoEm(LocalDateTime.now()); // Atualiza a data de modificação

        Produto updatedProduto = produtoService.save(existingProduto);

        return ResponseEntity.status(HttpStatus.OK).body(updatedProduto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduto(@PathVariable Long id) {
        produtoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}

