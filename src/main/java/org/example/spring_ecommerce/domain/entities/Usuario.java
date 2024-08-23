package org.example.spring_ecommerce.domain.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import org.example.spring_ecommerce.domain.entities.enums.ROLE;

import java.util.ArrayList;
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

        @JsonIgnore
        @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
        private List<Venda> vendas = new ArrayList<>();

        @Enumerated(EnumType.STRING)
        @Column(nullable = false, unique = true)
        private ROLE roles;

        public Usuario() {}

        public Usuario(String primeiroNome, String sobrenome, String senha, String email, ROLE roles) {
                this.primeiroNome = primeiroNome;
                this.sobrenome = sobrenome;
                this.senha = senha;
                this.email = email;
                this.roles = roles;
        }


        public Long getId() {
                return id;
        }

        public @NotEmpty String getPrimeiroNome() {
                return primeiroNome;
        }

        public @NotEmpty String getSobrenome() {
                return sobrenome;
        }

        public @NotEmpty String getSenha() {
                return senha;
        }

        public @Email @NotEmpty String getEmail() {
                return email;
        }

        public List<Venda> getVendas() {
                return vendas;
        }

        public ROLE getRoles() {
                return roles;
        }

        public void setPrimeiroNome(@NotEmpty String primeiroNome) {
                this.primeiroNome = primeiroNome;
        }

        public void setSobrenome(@NotEmpty String sobrenome) {
                this.sobrenome = sobrenome;
        }

        public void setSenha(@NotEmpty String senha) {
                this.senha = senha;
        }

        public void setEmail(@Email @NotEmpty String email) {
                this.email = email;
        }

        public void setRoles(ROLE roles) {
                this.roles = roles;
        }
}
