package org.example.spring_ecommerce.configuration.dataloader;


import org.example.spring_ecommerce.domain.entities.ItemVenda;
import org.example.spring_ecommerce.domain.entities.Produto;
import org.example.spring_ecommerce.domain.entities.Venda;
import org.example.spring_ecommerce.domain.entities.usuario.Usuario;
import org.example.spring_ecommerce.domain.repositories.ItemVendaRepository;
import org.example.spring_ecommerce.domain.repositories.ProdutoRepository;
import org.example.spring_ecommerce.domain.repositories.UsuarioRepository;
import org.example.spring_ecommerce.domain.repositories.VendaRepository;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class DataLoader {

/*
    @Bean
    public ApplicationRunner initUsuario(UsuarioRepository usuarioRepository,
                                         VendaRepository vendaRepository,
                                         ProdutoRepository produtoRepository,
                                         ItemVendaRepository itemVendaRepository) {

        return args -> {
            if (usuarioRepository.count() == 0) {
                Usuario usuario = usuarioRepository.save(new Usuario("teste","123","teste@gmail.com"));

                Venda venda = vendaRepository.save(new Venda(usuario, LocalDateTime.now(), 20.0));

                Produto produto = produtoRepository.save(new Produto("arroz",2.70,20, LocalDateTime.now(),LocalDateTime.now()));

                ItemVenda itemVenda = itemVendaRepository.save(new ItemVenda(produto,venda,20));
            }
        };

    }

     */
}

