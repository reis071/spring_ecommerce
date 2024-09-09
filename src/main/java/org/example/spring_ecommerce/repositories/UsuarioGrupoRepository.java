package org.example.spring_ecommerce.repositories;

import org.example.spring_ecommerce.model.usuario.Usuario;
import org.example.spring_ecommerce.model.usuario.UsuarioGrupo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsuarioGrupoRepository extends JpaRepository<UsuarioGrupo, Long> {
    @Query("""
            select distinct g.nome
            from UsuarioGrupo ug
            join ug.grupo g
            join ug.usuario u
            where u = ?1
    """)
    List<String> findPermissoesByUsuario(Usuario usuario);
}
