package org.example.spring_ecommerce.controllers;


import org.example.spring_ecommerce.model.Venda;

import org.example.spring_ecommerce.services.VendaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;


@RestController
@RequestMapping("vendas")
public class VendaController {

    @Autowired
    private VendaService vendaService;

    @PostMapping
    public ResponseEntity<Venda> createVenda(@RequestParam String nomeProduto,
                                             @RequestParam(required = false) Integer quantidade,
                                             @RequestParam Long usuarioId,Authentication authentication) {
        try {
            Venda novaVenda = vendaService.save(nomeProduto, quantidade, usuarioId);
            return ResponseEntity.status(HttpStatus.CREATED).body(novaVenda);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping
    public ResponseEntity<List<Venda>> getAllVendas(Authentication authentication) {
        List<Venda> vendas = vendaService.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(vendas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Venda> getVendaById(@PathVariable Long id) {
        try {
            Venda venda = vendaService.findById(id);
            return ResponseEntity.status(HttpStatus.OK).body(venda);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteVenda(@PathVariable Long id) {
        try {
            vendaService.delete(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
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

    @GetMapping("/relatorioPorData")
    public ResponseEntity<List<Venda>> getVendasByDate(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {
        List<Venda> vendas = vendaService.vendasPorDia(data);
        return ResponseEntity.status(HttpStatus.OK).body(vendas);
    }

    @GetMapping("/relatorioPorMesAno")
    public ResponseEntity<List<Venda>> getVendasByMonth(@RequestParam int ano, @RequestParam int mes) {
        List<Venda> vendas = vendaService.vendasPorMes(ano, mes);
        return ResponseEntity.status(HttpStatus.OK).body(vendas);
    }

    @GetMapping("/relatorioPorSemanaAtual")
    public ResponseEntity<List<Venda>> getVendasThisWeek() {
        List<Venda> vendas = vendaService.vendasPorSemanaAtual();
        return ResponseEntity.status(HttpStatus.OK).body(vendas);
    }

}
