package org.example.spring_ecommerce.domain.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome do produto é obrigatório.")
    @Size(max = 100, message = "O nome do produto não pode ter mais que 100 caracteres.")
    @Column(nullable = false, unique = true)
    private String nome;

    @NotNull(message = "O preço do produto é obrigatório.")
    @DecimalMin(value = "0.01", message = "O preço do produto deve ser maior que zero.")
    @Column(nullable = false)
    private double preco;

    @Min(value = 0, message = "O estoque do produto não pode ser negativo.")
    @Column(nullable = false)
    private int estoque;

    @Column(nullable = false)
    private boolean ativo = true;

    @JsonIgnore
    @OneToMany(mappedBy = "produto")
    private List<ItemVenda> itensVenda = new ArrayList<>();

    @Column(nullable = false)
    private LocalDateTime criadoEm;

    @Column(nullable = false)
    private LocalDateTime atualizadoEm;

    public Produto() {}

    public Produto(String nome, double preco, Integer estoque,  LocalDateTime criadoEm, LocalDateTime atualizadoEm) {
        this.nome = nome;
        this.preco = preco;
        this.estoque = estoque;
        this.ativo = true;
        this.criadoEm = criadoEm;
        this.atualizadoEm = atualizadoEm;
    }

}
