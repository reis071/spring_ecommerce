package org.example.spring_ecommerce.domain.repositories;

import org.example.spring_ecommerce.domain.entities.Produto;
import org.springframework.data.repository.CrudRepository;

public interface ProdutoRepository extends CrudRepository<Produto, Long> {
}
