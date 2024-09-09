package org.example.spring_ecommerce.controllers;

import lombok.RequiredArgsConstructor;
import org.example.spring_ecommerce.model.Carrinho;
import org.example.spring_ecommerce.model.Venda;

import org.example.spring_ecommerce.services.CompraService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/compras")
@RequiredArgsConstructor
public class CompraController {

    private final CompraService compraService;



    @PostMapping("comprar-direto")
    public ResponseEntity<Venda> comprarProduto(@RequestParam String nomeProd, @RequestParam int quantidade) {
        Venda venda = compraService.compra(nomeProd, quantidade);
        return ResponseEntity.ok(venda);
    }

    // Endpoint para adicionar produto ao carrinho
    @PostMapping("adicionar-carrinho")
    public ResponseEntity<Carrinho> adicionarAoCarrinho(@RequestParam String nomeProd, @RequestParam int quantidade) {
        Carrinho carrinho = compraService.adicionarAoCarrinho(nomeProd, quantidade);

        return ResponseEntity.status(HttpStatus.CREATED).body(carrinho);
    }

    // Endpoint para finalizar a compra de todos os itens do carrinho
    @PostMapping("finalizar-compra")
    public ResponseEntity<String> finalizarCompra() {
        Venda venda = compraService.finalizarCompra();
        return ResponseEntity.status(HttpStatus.OK).body("compra feita com sucesso!");
    }

    @DeleteMapping("remover-produto-carrinho")
    public ResponseEntity<Void> removerProdutoDoCarrinhoPorNome(@RequestParam String nomeProduto,
                                                                @RequestParam int quantidade) {
        compraService.removerProdutoDoCarrinhoPorNome(nomeProduto, quantidade);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("valor-total-carrinho")
    public ResponseEntity<String> valorTotalCarrinho() {
        return  ResponseEntity.status(HttpStatus.ACCEPTED).body(compraService.precoTotalCarrinho());
    }

    @GetMapping("visualizar-carrinho")
    public ResponseEntity<Carrinho> visualizarCarrinho() {
        return  ResponseEntity.status(HttpStatus.ACCEPTED).body(compraService.visualizarCarrinho());
    }
}
