package org.example.spring_ecommerce.services;

import org.example.spring_ecommerce.model.usuario.Grupo;
import org.example.spring_ecommerce.repositories.GrupoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GrupoService {

    private final GrupoRepository grupoRepository;

    public GrupoService(GrupoRepository grupoRepository) {
        this.grupoRepository = grupoRepository;
    }


    public Grupo save(Grupo grupo) {
        if(grupoRepository.findByNome(grupo.getNome()).isPresent() ) {
            return null;
        }
        return grupoRepository.save(grupo);
    }

    public List<Grupo> findAll() {
        return grupoRepository.findAll();
    }
}
