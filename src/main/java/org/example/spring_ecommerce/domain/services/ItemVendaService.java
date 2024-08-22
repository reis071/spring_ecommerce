package org.example.spring_ecommerce.domain.services;

import org.example.spring_ecommerce.domain.entities.ItemVenda;
import org.example.spring_ecommerce.domain.repositories.ItemVendaRepository;
import org.springframework.stereotype.Service;

@Service
public class ItemVendaService {

    private ItemVendaRepository itemVendaRepository;

    public ItemVendaService(ItemVendaRepository itemVendaRepository) {
        this.itemVendaRepository = itemVendaRepository;
    }

    public ItemVenda save(ItemVenda itemVenda) {
        return itemVendaRepository.save(itemVenda);
    }
}
