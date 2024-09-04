package org.example.spring_ecommerce.repositories;

import org.example.spring_ecommerce.model.Venda;
import org.example.spring_ecommerce.model.usuario.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface VendaRepository  extends JpaRepository<Venda, Long> {
    List<Venda> findByDataVendaBetween(LocalDateTime start, LocalDateTime end);

    List<Venda> findByUsuarioEmail(String email);
}
