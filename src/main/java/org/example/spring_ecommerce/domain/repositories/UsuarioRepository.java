package org.example.spring_ecommerce.domain.repositories;

import org.example.spring_ecommerce.domain.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
}
