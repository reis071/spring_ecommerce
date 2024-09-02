package org.example.spring_ecommerce.services;

import lombok.RequiredArgsConstructor;
import org.example.spring_ecommerce.configuration.advices.exceptionExclusives.ProdutoInativo;
import org.example.spring_ecommerce.model.ItemVenda;
import org.example.spring_ecommerce.model.Produto;
import org.example.spring_ecommerce.model.Venda;
import org.example.spring_ecommerce.model.enums.StatusVenda;
import org.example.spring_ecommerce.model.usuario.Usuario;
import org.example.spring_ecommerce.repositories.ItemVendaRepository;
import org.example.spring_ecommerce.repositories.ProdutoRepository;
import org.example.spring_ecommerce.repositories.UsuarioRepository;
import org.example.spring_ecommerce.repositories.VendaRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VendaService {

    private final ProdutoRepository produtoRepository;
    private final UsuarioRepository usuarioRepository;
    private final ItemVendaRepository itemVendaRepository;
    private final VendaRepository vendaRepository;

    //adiciona venda
    @CacheEvict(value = "vendaCache", allEntries = true)
    public Venda save(String nomeProd, Integer quantidade, Long usuarioId) {
        Produto produtoAtual = produtoRepository.findByNome(nomeProd)
                .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado"));

        if (!produtoAtual.isAtivo()){
            throw new ProdutoInativo();
        }

        if (quantidade == null || quantidade <= 0 || quantidade > produtoAtual.getEstoque()) {
            throw new IllegalArgumentException("Quantidade inválida, estoque insuficiente");
        }

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

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

    //retorna todas as vendas
    @Cacheable("vendaCache")
    public List<Venda> findAll() {
        return  vendaRepository.findAll();
    }


    //retorna uma venda por id
    @Cacheable("vendaCache")
    public Venda findById(Long id) {
        Optional<Venda> vendaOptional = vendaRepository.findById(id);
        return vendaOptional.orElseThrow(() -> new RuntimeException("Venda não encontrada"));
    }

    //atualiza uma venda
    @CacheEvict(value = "vendaCache", allEntries = true)
    public Venda update(Long id, String produtoNome, Integer quantidade, Long usuarioId) {
        Venda venda = vendaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Venda não encontrada"));

        Produto produtoAtual = produtoRepository.findByNome(produtoNome)
                .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado"));

        if (quantidade == null || quantidade <= 0 || quantidade > produtoAtual.getEstoque()) {
            throw new IllegalArgumentException("Quantidade inválida ou estoque insuficiente");
        }

        usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        venda.setDataVenda(LocalDateTime.now());
        venda.setValorTotal(quantidade * produtoAtual.getPreco());


        ItemVenda itemVenda = venda.getItensVenda().stream()
                .filter(item -> item.getProduto().equals(produtoAtual))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado na venda"));

        itemVenda.setQuantidade(quantidade);

        produtoAtual.setEstoque(produtoAtual.getEstoque() - quantidade);

        produtoRepository.save(produtoAtual);
        itemVendaRepository.save(itemVenda);

        return vendaRepository.save(venda);
    }

    //relatorio vendas por data
    @Cacheable("vendaCache")
    public List<Venda> vendasPorDia(LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = startOfDay.plusDays(1).minusNanos(1);
        return vendaRepository.findByDataVendaBetween(startOfDay, endOfDay);
    }

    //relatorio vendas por mes
    @Cacheable("vendaCache")
    public List<Venda> vendasPorMes(int year, int month) {
        LocalDateTime startOfMonth = LocalDate.of(year, month, 1).atStartOfDay();
        LocalDateTime endOfMonth = startOfMonth.plusMonths(1).minusNanos(1);
        return vendaRepository.findByDataVendaBetween(startOfMonth, endOfMonth);
    }

    // relatorio venda por semana
    @Cacheable("vendaCache")
    public List<Venda> vendasPorSemanaAtual() {
        LocalDate now = LocalDate.now();
        LocalDate startOfWeek = now.with(DayOfWeek.MONDAY);
        LocalDate endOfWeek = now.with(DayOfWeek.SUNDAY);

        LocalDateTime startOfWeekDateTime = startOfWeek.atStartOfDay();
        LocalDateTime endOfWeekDateTime = endOfWeek.atTime(LocalTime.MAX);

        return vendaRepository.findByDataVendaBetween(startOfWeekDateTime, endOfWeekDateTime).stream()
                .filter(venda -> {
                    LocalDate vendaDate = venda.getDataVenda().toLocalDate();
                    return !vendaDate.getDayOfWeek().equals(DayOfWeek.SATURDAY) && !vendaDate.getDayOfWeek().equals(DayOfWeek.SUNDAY);
                })
                .collect(Collectors.toList());
    }

    //deletar venda
    @CacheEvict(value = "vendaCache", allEntries = true)
    public void delete(Long id) {
        vendaRepository.deleteById(id);
    }
}
