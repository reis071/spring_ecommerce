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

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Produto> addProduto(@RequestBody Produto produto) {
        Produto novoProduto = produtoService.save(produto);

        return ResponseEntity.status(HttpStatus.CREATED).body(novoProduto);
    }

    @GetMapping("/procurarProduto")
    public ResponseEntity<Produto> getProduto(@RequestParam String nomeProduto) {
        return ResponseEntity.status(HttpStatus.OK).body(produtoService.procurarProdutoPorNome(nomeProduto));
    }

    @GetMapping
    public ResponseEntity<List<Produto>> getAllProdutos() {

        return ResponseEntity.status(HttpStatus.OK).body(produtoService.findAll());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Produto> atualizarProduto(@PathVariable Long id, @RequestBody Produto produtoAtualizado) {
        Produto produtoAtualizadoRetorno = produtoService.atualizarProduto(id, produtoAtualizado);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(produtoAtualizadoRetorno);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/deletarProduto")
    public ResponseEntity<Void> deleteProduto(@RequestParam String nomeProduto) {
        produtoService.deleteById(nomeProduto);
        return ResponseEntity.noContent().build();
    }

}

