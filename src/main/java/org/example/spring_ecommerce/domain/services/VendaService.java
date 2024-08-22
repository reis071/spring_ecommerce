package org.example.spring_ecommerce.domain.services;

import org.example.spring_ecommerce.domain.entities.Venda;
import org.example.spring_ecommerce.domain.repositories.VendaRepository;
import org.springframework.stereotype.Service;

@Service
public class VendaService {

    private VendaRepository vendaRepository;

    public VendaService(VendaRepository vendaRepository) {
        this.vendaRepository = vendaRepository;
    }

    public Venda save(Venda venda) {
        return vendaRepository.save(venda);
    }

}
