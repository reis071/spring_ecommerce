package org.example.spring_ecommerce.domain.repositories;

import org.example.spring_ecommerce.domain.entities.Venda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface VendaRepository  extends JpaRepository<Venda, Long> {
    List<Venda> findByDataVendaBetween(LocalDateTime start, LocalDateTime end);
}
