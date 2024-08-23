package org.example.spring_ecommerce.domain.services;

import org.example.spring_ecommerce.domain.entities.ItemVenda;
import org.example.spring_ecommerce.domain.entities.Produto;
import org.example.spring_ecommerce.domain.entities.Usuario;
import org.example.spring_ecommerce.domain.entities.Venda;
import org.example.spring_ecommerce.domain.repositories.ItemVendaRepository;
import org.example.spring_ecommerce.domain.repositories.ProdutoRepository;
import org.example.spring_ecommerce.domain.repositories.UsuarioRepository;
import org.example.spring_ecommerce.domain.repositories.VendaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class VendaService {




    private final ProdutoRepository produtoRepository;
    private final UsuarioRepository usuarioRepository;
    private final ItemVendaRepository itemVendaRepository;
    private final VendaRepository vendaRepository;

    @Autowired
    public VendaService(ProdutoRepository produtoRepository, UsuarioRepository usuarioRepository,
                        ItemVendaRepository itemVendaRepository, VendaRepository vendaRepository) {
        this.produtoRepository = produtoRepository;
        this.usuarioRepository = usuarioRepository;
        this.itemVendaRepository = itemVendaRepository;
        this.vendaRepository = vendaRepository;
    }

    public Venda save(String nomeProd, Integer quantidade, Long usuarioId) {
        // Verificar se o produto existe pelo nome
        Produto produtoAtual = produtoRepository.findByNome(nomeProd)
                .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado"));

        // Verificar se a quantidade é válida e se o estoque é suficiente
        if (quantidade == null || quantidade <= 0 || quantidade > produtoAtual.getEstoque()) {
            throw new IllegalArgumentException("Quantidade inválida ou estoque insuficiente");
        }

        // Verificar se o usuário existe
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        // Criar e salvar a Venda
        Venda venda = new Venda(usuario, LocalDateTime.now(), quantidade * produtoAtual.getPreco());
        venda = vendaRepository.save(venda);

        // Atualizar o estoque do produto
        produtoAtual.setEstoque(produtoAtual.getEstoque() - quantidade);
        produtoRepository.save(produtoAtual);

        // Criar o ItemVenda e associar à venda
        ItemVenda itemVenda = new ItemVenda(produtoAtual, venda, quantidade);
        venda.getItensVenda().add(itemVenda);

        // Salvar o ItemVenda
        itemVendaRepository.save(itemVenda);

        // Retornar a Venda
        return venda;
    }

    public List<Venda> findAll() {
        return vendaRepository.findAll();
    }

    public Venda findById(Long id) {
        Optional<Venda> vendaOptional = vendaRepository.findById(id);
        return vendaOptional.orElseThrow(() -> new RuntimeException("Venda não encontrada"));
    }

    public  void delete(Long id) {
        vendaRepository.deleteById(id);
    }

    public Venda update(Long id, String produtoNome, Integer quantidade, Long usuarioId) {
        Venda venda = vendaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Venda não encontrada"));

        Produto produtoAtual = produtoRepository.findByNome(produtoNome)
                .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado"));

        if (quantidade == null || quantidade <= 0 || quantidade > produtoAtual.getEstoque()) {
            throw new IllegalArgumentException("Quantidade inválida ou estoque insuficiente");
        }

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        venda.setDataVenda(LocalDateTime.now());
        venda.setValorTotal(quantidade * produtoAtual.getPreco());

        // Atualizar itens de venda
        ItemVenda itemVenda = venda.getItensVenda().stream()
                .filter(item -> item.getProduto().equals(produtoAtual))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado na venda"));

        itemVenda.setQuantidade(quantidade);

        // Atualizar estoque do produto
        produtoAtual.setEstoque(produtoAtual.getEstoque() - quantidade);

        produtoRepository.save(produtoAtual);
        itemVendaRepository.save(itemVenda);

        return vendaRepository.save(venda);
    }

}
