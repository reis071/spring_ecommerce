package org.example.spring_ecommerce.domain.controllers;

import lombok.RequiredArgsConstructor;
import org.example.spring_ecommerce.domain.entities.Produto;
import org.example.spring_ecommerce.domain.services.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;


@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    @Autowired
    private ProdutoService produtoService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/add")
    public ResponseEntity<Produto> addProduto(@RequestBody Produto produto) {
        Produto novoProduto = produtoService.save(produto);

        return ResponseEntity.status(HttpStatus.CREATED).body(novoProduto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Produto> getProduto(@PathVariable Long id) {
        return ResponseEntity.ok(produtoService.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<Produto>> getAllProdutos() {
        return ResponseEntity.ok(produtoService.findAll());
    }

    @PutMapping("atualizar/{id}")
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

        return ResponseEntity.ok(updatedProduto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduto(@PathVariable Long id) {
        produtoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}

