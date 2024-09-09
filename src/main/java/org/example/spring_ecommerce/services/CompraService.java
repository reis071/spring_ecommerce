package org.example.spring_ecommerce.services;

import lombok.RequiredArgsConstructor;
import org.example.spring_ecommerce.configuration.advices.exceptionExclusives.ProdutoInativo;
import org.example.spring_ecommerce.model.*;
import org.example.spring_ecommerce.model.enums.StatusVenda;
import org.example.spring_ecommerce.model.usuario.Usuario;
import org.example.spring_ecommerce.repositories.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CompraService {

    private final ProdutoRepository produtoRepository;
    private final VendaRepository vendaRepository;
    private final ItemVendaRepository itemVendaRepository;
    private final UsuarioRepository usuarioRepository;
    private final CarrinhoRepository carrinhoRepository;
    private final ItemCarrinhoRepository itemCarrinhoRepository;



    public Venda compra(String nomeProd, int quantidade){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com email: " + email));

        Produto produtoAtual = produtoRepository.findByNome(nomeProd)
                .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado"));

        if (!produtoAtual.isAtivo()){
            throw new ProdutoInativo();
        }

        if (quantidade <= 0 || quantidade > produtoAtual.getEstoque()) {
            throw new IllegalArgumentException("Quantidade inválida, estoque insuficiente");
        }


        Venda venda = new Venda(usuario, LocalDateTime.now(), quantidade * produtoAtual.getPreco());
        venda.setStatus(StatusVenda.VENDIDO);

        if(usuario.getSaldo() >= (quantidade * produtoAtual.getPreco())){
            usuario.setSaldo(usuario.getSaldo() - quantidade * produtoAtual.getPreco());
            usuarioRepository.save(usuario);
            vendaRepository.save(venda);
        }
        else {
            venda.setStatus(StatusVenda.CANCELADA);
            vendaRepository.save(venda);
            throw new IllegalArgumentException("Saldo insuficiente");
        }

        produtoAtual.setEstoque(produtoAtual.getEstoque() - quantidade);
        produtoRepository.save(produtoAtual);

        ItemVenda itemVenda = new ItemVenda(produtoAtual, venda, quantidade);
        venda.getItensVenda().add(itemVenda);

        itemVendaRepository.save(itemVenda);

        return venda;
    }

    // Método para adicionar um item ao carrinho
    public Carrinho adicionarAoCarrinho(String nomeProd, int quantidade) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com email: " + email));

        Produto produtoAtual = produtoRepository.findByNome(nomeProd)
                .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado"));

        if (!produtoAtual.isAtivo()) {
            throw new ProdutoInativo();
        }

        if (quantidade <= 0 || quantidade > produtoAtual.getEstoque()) {
            throw new IllegalArgumentException("Quantidade inválida, estoque insuficiente");
        }


        Carrinho carrinho = carrinhoRepository.findByUsuario(usuario)
                .orElseGet(() -> carrinhoRepository.save(new Carrinho(usuario)));

        // Adiciona o item ao carrinho
        ItemCarrinho itemCarrinho = new ItemCarrinho(carrinho, produtoAtual, quantidade);
        itemCarrinhoRepository.save(itemCarrinho);
        carrinho.getItens().add(itemCarrinho);

        return carrinho;
    }

    // Método para comprar os itens do carrinho
    public Venda finalizarCompra() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com email: " + email));

        Carrinho carrinho = carrinhoRepository.findByUsuario(usuario)
                .orElseThrow(() -> new IllegalArgumentException("Carrinho vazio ou não encontrado"));

        List<ItemCarrinho> itensCarrinho = carrinho.getItens();
        if (itensCarrinho.isEmpty()) {
            throw new IllegalArgumentException("O carrinho está vazio.");
        }

        double valorTotal = itensCarrinho.stream()
                .mapToDouble(item -> item.getProduto().getPreco() * item.getQuantidade())
                .sum();

        if (usuario.getSaldo() < valorTotal) {
            throw new IllegalArgumentException("Saldo insuficiente para completar a compra.");
        }

        // Cria a venda
        Venda venda = new Venda(usuario, LocalDateTime.now(), valorTotal);
        venda.setStatus(StatusVenda.VENDIDO);
        vendaRepository.save(venda);

        // Processa cada item do carrinho
        for (ItemCarrinho itemCarrinho : itensCarrinho) {
            Produto produto = itemCarrinho.getProduto();
            int quantidade = itemCarrinho.getQuantidade();

            if (quantidade > produto.getEstoque()) {
                throw new IllegalArgumentException("Estoque insuficiente para o produto: " + produto.getNome());
            }

            produto.setEstoque(produto.getEstoque() - quantidade);
            produtoRepository.save(produto);

            ItemVenda itemVenda = new ItemVenda(produto, venda, quantidade);
            itemVendaRepository.save(itemVenda);
            venda.getItensVenda().add(itemVenda);
        }

        // Deduz o saldo do usuário e atualiza
        usuario.setSaldo(usuario.getSaldo() - valorTotal);
        usuarioRepository.save(usuario);

        // Limpa o carrinho após a compra
        carrinho.getItens().clear();
        carrinhoRepository.save(carrinho);

        return venda;
    }

    // Remover produto do carrinho pelo nome
    public void removerProdutoDoCarrinhoPorNome(String nomeProduto, int quantidade) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com email: " + email));

        Carrinho carrinho = carrinhoRepository.findByUsuario(usuario)
                .orElseThrow(() -> new IllegalArgumentException("Carrinho não encontrado para o usuário"));

        Produto produto = produtoRepository.findByNome(nomeProduto)
                .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado com o nome: " + nomeProduto));

        ItemCarrinho itemCarrinho = itemCarrinhoRepository.findByCarrinhoAndProduto(carrinho, produto)
                .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado no carrinho"));

        int quantidadeAtual = itemCarrinho.getQuantidade();

        if(quantidade ==0){
            throw new IllegalArgumentException("Quantidade não pode ser igual a 0");
        }else if(quantidade > quantidadeAtual){
            throw  new IllegalArgumentException("Quantidade passada é maior que a quantidade do produto que esta no carrinho!");
        }
        if (quantidadeAtual == quantidade) {
            itemCarrinhoRepository.delete(itemCarrinho);
        } else {
            itemCarrinho.setQuantidade(quantidadeAtual - quantidade);
            itemCarrinhoRepository.save(itemCarrinho);
        }
    }

    //visualizar preço total do carrinho
    public String precoTotalCarrinho(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com email: " + email));

        Carrinho carrinho = carrinhoRepository.findByUsuario(usuario)
                .orElseThrow(() -> new IllegalArgumentException("Carrinho vazio ou não encontrado"));

        List<ItemCarrinho> itensCarrinho = carrinho.getItens();
        if (itensCarrinho.isEmpty()) {
            throw new IllegalArgumentException("O carrinho está vazio.");
        }

        double valorTotal = itensCarrinho.stream()
                .mapToDouble(item -> item.getProduto().getPreco() * item.getQuantidade())
                .sum();

        return "{valorTotal: " + valorTotal + "}";
    }

    public Carrinho visualizarCarrinho(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com email: " + email));

        Carrinho carrinho = carrinhoRepository.findByUsuario(usuario)
                .orElseGet(() -> carrinhoRepository.save(new Carrinho(usuario)));

        return carrinho;
    }

}
