package org.example.spring_ecommerce.configuration.dataloader;


import org.example.spring_ecommerce.model.usuario.Grupo;
import org.example.spring_ecommerce.repositories.GrupoRepository;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class DataLoader {

    @Bean
    public ApplicationRunner initUsuario(GrupoRepository grupoRepository) {

        return args -> {
            grupoRepository.save(new Grupo("ADMIN"));
            grupoRepository.save(new Grupo("USER"));
        };

    }


}

