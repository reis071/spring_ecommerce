package org.example.spring_ecommerce.repositories;

import org.example.spring_ecommerce.model.ItemVenda;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemVendaRepository extends JpaRepository<ItemVenda, Long> {
}
