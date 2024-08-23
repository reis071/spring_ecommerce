package org.example.spring_ecommerce.domain.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Venda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;


    @Column(nullable = false)
    private LocalDateTime dataVenda;

    @Column(nullable = false)
    private double valorTotal;

    @OneToMany(mappedBy = "venda", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<ItemVenda> itensVenda = new ArrayList<>();

    public Venda() { }

    public Venda(Usuario usuario, LocalDateTime dataVenda, double valorTotal) {
        this.usuario = usuario;
        this.dataVenda = dataVenda;
        this.valorTotal = valorTotal;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public void setDataVenda(LocalDateTime dataVenda) {
        this.dataVenda = dataVenda;
    }

    public void setValorTotal(double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public Long getId() {
        return id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public List<ItemVenda> getItensVenda() {
        return itensVenda;
    }

    public LocalDateTime getDataVenda() {
        return dataVenda;
    }

    public double getValorTotal() {
        return valorTotal;
    }
}
