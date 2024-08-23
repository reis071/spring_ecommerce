package org.example.spring_ecommerce.domain.repositories;

import org.example.spring_ecommerce.domain.entities.ItemVenda;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemVendaRepository extends JpaRepository<ItemVenda, Long> {
}
