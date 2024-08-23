package org.example.spring_ecommerce.domain.controllers;

import org.example.spring_ecommerce.domain.entities.Produto;
import org.example.spring_ecommerce.domain.entities.Venda;
import org.example.spring_ecommerce.domain.services.ProdutoService;
import org.example.spring_ecommerce.domain.services.VendaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/vendas")
public class VendaController {

    @Autowired
    private VendaService vendaService;

    @PostMapping(path = "/add")
    public ResponseEntity<Venda> createVenda(@RequestParam String produtoNome,
                                             @RequestParam(required = false) Integer quantidade,
                                             @RequestParam Long usuarioId) {
        try {
            Venda novaVenda = vendaService.save(produtoNome, quantidade, usuarioId);
            return ResponseEntity.status(HttpStatus.CREATED).body(novaVenda);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping
    public ResponseEntity<List<Venda>> getAllVendas() {
        List<Venda> vendas = vendaService.findAll();
        return ResponseEntity.ok(vendas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Venda> getVendaById(@PathVariable Long id) {
        try {
            Venda venda = vendaService.findById(id);
            return ResponseEntity.ok(venda);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVenda(@PathVariable Long id) {
        try {
            vendaService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Venda> updateVenda(@PathVariable Long id,
                                             @RequestParam String produtoNome,
                                             @RequestParam Integer quantidade,
                                             @RequestParam Long usuarioId) {
        try {
            Venda vendaAtualizada = vendaService.update(id, produtoNome, quantidade, usuarioId);
            return ResponseEntity.ok(vendaAtualizada);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

}
