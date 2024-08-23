package org.example.spring_ecommerce.domain.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.math.BigDecimal;

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


    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public void setVenda(Venda venda) {
        this.venda = venda;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }


}
