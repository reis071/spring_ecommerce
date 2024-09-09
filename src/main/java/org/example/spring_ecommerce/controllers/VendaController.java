package org.example.spring_ecommerce.controllers;


import org.example.spring_ecommerce.model.Venda;

import org.example.spring_ecommerce.services.VendaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;


@RestController
@RequestMapping("vendas")
public class VendaController {

    @Autowired
    private VendaService vendaService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<Venda>> pegarTodasVendas() {
        List<Venda> vendas = vendaService.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(vendas);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/buscarVenda")
    public ResponseEntity<List<Venda>> buscarVendasPorEmail(@RequestParam String email) {
        List<Venda> vendas = vendaService.buscarVendasPorEmail(email);
        return ResponseEntity.ok(vendas);
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

    @PreAuthorize("hasRole('ADMIN')")
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

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/relatorioPorData")
    public ResponseEntity<String> getVendasByDate(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {
        String vendas = vendaService.vendasPorDia(data);
        return ResponseEntity.status(HttpStatus.OK).body(vendas);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/relatorioPorMesAno")
    public ResponseEntity<String> getVendasByMonth(@RequestParam int ano, @RequestParam int mes) {
        String vendas = vendaService.vendasPorMes(ano, mes);
        return ResponseEntity.status(HttpStatus.OK).body(vendas);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/relatorioPorSemanaAtual")
    public ResponseEntity<String> getVendasThisWeek() {
        String vendas = vendaService.vendasPorSemanaAtual();
        return ResponseEntity.status(HttpStatus.OK).body(vendas);
    }

}
