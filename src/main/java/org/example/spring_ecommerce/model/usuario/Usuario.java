package org.example.spring_ecommerce.model.usuario;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.spring_ecommerce.model.Venda;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


import java.util.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Usuario{

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @NotEmpty
        @Column(nullable = false, unique = true)
        private String nome;


        @NotEmpty
        @Column(nullable = false)
        private String senha;

        @Email
        @NotEmpty
        @Column(nullable = false, unique = true)
        private String email;

        @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
        @JsonIgnore // Prevent serialization issues
        private List<Venda> vendas = new ArrayList<>();

        @Transient
        private List<String> permissoes;


}
