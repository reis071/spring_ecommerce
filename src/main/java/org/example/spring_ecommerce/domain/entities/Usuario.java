package org.example.spring_ecommerce.domain.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import org.example.spring_ecommerce.domain.entities.enums.ROLE;

import java.util.List;

@Entity
public class Usuario {


        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @NotEmpty
        @Column(nullable = false, unique = true)
        private String primeiroNome;

        @NotEmpty
        @Column(nullable = false, unique = true)
        private String sobrenome;

        @NotEmpty
        @Column(nullable = false)
        private String senha;

        @Email
        @NotEmpty
        @Column(nullable = false)
        private String email;

        @OneToMany(mappedBy = "usuario")
        private List<Venda> vendas;

        @Enumerated(EnumType.STRING)
        @Column(nullable = false, unique = true)
        private ROLE roles;

    }
