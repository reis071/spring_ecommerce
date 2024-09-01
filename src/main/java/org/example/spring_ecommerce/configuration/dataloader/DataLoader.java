package org.example.spring_ecommerce.configuration.dataloader;


import org.springframework.stereotype.Component;

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

