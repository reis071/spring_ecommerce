package org.example.spring_ecommerce.services;

import lombok.RequiredArgsConstructor;
import org.example.spring_ecommerce.configuration.advices.exceptionExclusives.ErroAutenticacao;
import org.example.spring_ecommerce.configuration.advices.exceptionExclusives.ProdutoInativo;
import org.example.spring_ecommerce.configuration.advices.exceptionExclusives.TokenInvalido;
import org.example.spring_ecommerce.configuration.advices.exceptionExclusives.UsuarioNaoAutenticado;
import org.example.spring_ecommerce.controllers.dto.EmailDto;
import org.example.spring_ecommerce.controllers.dto.UsuarioDto;
import org.example.spring_ecommerce.model.ItemVenda;
import org.example.spring_ecommerce.model.Produto;
import org.example.spring_ecommerce.model.Venda;
import org.example.spring_ecommerce.model.enums.StatusVenda;
import org.example.spring_ecommerce.model.usuario.Grupo;
import org.example.spring_ecommerce.model.usuario.Usuario;
import org.example.spring_ecommerce.model.usuario.UsuarioGrupo;
import org.example.spring_ecommerce.repositories.*;
import org.example.spring_ecommerce.configuration.security.jwt.JwtService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UsuarioService implements UserDetailsService {

    private final ProdutoRepository produtoRepository;
    private final VendaRepository vendaRepository;
    private final ItemVendaRepository itemVendaRepository;
    private final UsuarioRepository usuarioRepository;
    private final GrupoRepository grupoRepository;
    private final UsuarioGrupoRepository usuarioGrupoRepository;

    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final JwtService jwtService;


    //Cadastra Usuario
    public Usuario salvar(Usuario usuario, List<String> grupos){
        String senhaCriptografada = passwordEncoder.encode(usuario.getSenha());
        usuario.setSenha(senhaCriptografada);


        List<UsuarioGrupo> listaUsuarioGrupo = grupos.stream().map(nomeGrupo -> {
                    Optional<Grupo> possivelGrupo = grupoRepository.findByNome(nomeGrupo);
                    if (possivelGrupo.isPresent()) {
                        Grupo grupo = possivelGrupo.get();
                        usuarioRepository.save(usuario);
                        return new UsuarioGrupo(usuario, grupo);
                    }
                    throw new IllegalStateException("Grupo não presente");
                })
                .filter(grupo -> grupo != null)
                .collect(Collectors.toList());

        usuarioGrupoRepository.saveAll(listaUsuarioGrupo);



        return usuario;
    }

    //autentica usuario
    public UserDetails autenticar( Usuario usuario ){
        UserDetails user = loadUserByUsername(usuario.getEmail());
        boolean senhasBatem = passwordEncoder.matches( usuario.getSenha(), user.getPassword() );
        if(senhasBatem){
            return user;
        }
        throw new ErroAutenticacao();
    }

    public UsuarioDto obterUsuarioComPermissoes(String email) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findByEmail(email);
        if (usuarioOptional.isEmpty()) {
            return null;
        }

        Usuario usuario = usuarioOptional.get();
        List<String> permissoes = usuarioGrupoRepository.findPermissoesByUsuario(usuario);
        return new UsuarioDto(usuario, permissoes); // Retorna um UsuarioDto com permissões
    }

    //valida

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado na base de dados."));
        List<String> permissoes = usuarioGrupoRepository.findPermissoesByUsuario(usuario);
        return new UsuarioDto(usuario, permissoes); // Retorna um UsuarioDto com permissões
    }

    //envia token para alterar senha
    public void enviarSolicitacaoDeResetarSenha(String email) {
        Usuario user = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com email: " + email));

        String token = jwtService.gerarToken(new UsuarioDto(user,user.getPermissoes()));

        EmailDto emailDtoDetails = new EmailDto(
                user.getEmail(),
                "Reset de Senha",
                "Para redefinir sua senha:"
                        + "token=" + token
        );

        emailService.sendEmail(emailDtoDetails); // Corrigido: Método correto
    }

    //valida se é um token valido e reseta a senha
    public void resetarSenha(String token, String newPassword) {
        if (!jwtService.tokenValido(token)) {
            throw new TokenInvalido();
        }

        String email = jwtService.obterLoginUsuario(token);

        Usuario user = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + email));

        if (usuarioAutenticado(user.getEmail())) {
            throw new IllegalStateException("Usuário está logado e não pode alterar a senha.");
        }

        user.setSenha(passwordEncoder.encode(newPassword));
        usuarioRepository.save(user);
    }

    public void depositar(double deposito,String email){
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com email: " + email));
        if(usuarioAutenticado(usuario.getEmail())){

        if(deposito > 0){
            usuario.setSaldo(usuario.getSaldo() + deposito);
            usuarioRepository.save(usuario);
        }else{
            throw new RuntimeException("Saldo não pode ser negativo");
        }
        }
    }

    //verifica se o Usuario esta autenticado
    private boolean usuarioAutenticado(String email) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.isAuthenticated() && email.equals(authentication.getName());
    }

    //fazer compra
    public Venda compra(String email, String nomeProd, int quantidade){
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        if (usuarioAutenticado(usuario.getEmail())){


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
        throw new UsuarioNaoAutenticado();
    }

}

