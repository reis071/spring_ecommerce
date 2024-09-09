package org.example.spring_ecommerce.repositories;

import org.example.spring_ecommerce.model.Carrinho;
import org.example.spring_ecommerce.model.ItemCarrinho;
import org.example.spring_ecommerce.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface ItemCarrinhoRepository  extends JpaRepository<ItemCarrinho, Long> {

    // Método personalizado para encontrar um item do carrinho com base no carrinho e no produto
    Optional<ItemCarrinho> findByCarrinhoAndProduto(Carrinho carrinho, Produto produto);

}
