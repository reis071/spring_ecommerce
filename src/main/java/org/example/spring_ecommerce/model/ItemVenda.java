package org.example.spring_ecommerce.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class ItemVenda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne
    @JoinColumn(name = "id_Produto", nullable = false)
    private Produto produto;

    @ManyToOne
    @JoinColumn(name = "venda_id", nullable = false)
    @JsonBackReference // Adiciona esta anotação
    private Venda venda;

    @Column(nullable = false)
    private Integer quantidade;


    public ItemVenda() {}

    public ItemVenda(Produto produto, Venda venda, Integer quantidade) {
        this.produto = produto;
        this.venda = venda;
        this.quantidade = quantidade;
    }

    public Long getId() {
        return id;
    }

    public Produto getProduto() {
        return produto;
    }

    public Venda getVenda() {
        return venda;
    }

    public Integer getQuantidade() {
        return quantidade;
    }




}
