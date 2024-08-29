package org.example.spring_ecommerce.domain.services;

import org.example.spring_ecommerce.domain.entities.usuario.Grupo;
import org.example.spring_ecommerce.domain.repositories.GrupoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GrupoService {

    private GrupoRepository grupoRepository;

    public GrupoService(GrupoRepository grupoRepository) {
        this.grupoRepository = grupoRepository;
    }


    public Grupo save(Grupo grupo) {
        return grupoRepository.save(grupo);
    }

    public List<Grupo> findAll() {
        return grupoRepository.findAll();
    }
}
