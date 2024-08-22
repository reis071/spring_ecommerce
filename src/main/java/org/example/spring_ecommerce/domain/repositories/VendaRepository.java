package org.example.spring_ecommerce.domain.repositories;

import org.example.spring_ecommerce.domain.entities.Venda;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VendaRepository  extends CrudRepository<Venda, Long> {
}
