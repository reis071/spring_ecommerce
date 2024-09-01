package org.example.spring_ecommerce.services;

import lombok.RequiredArgsConstructor;
import org.example.spring_ecommerce.configuration.advices.exceptionExclusives.ErroAutenticacao;
import org.example.spring_ecommerce.configuration.advices.exceptionExclusives.TokenInvalido;
import org.example.spring_ecommerce.controllers.dto.EmailDto;
import org.example.spring_ecommerce.controllers.dto.UsuarioDto;
import org.example.spring_ecommerce.model.usuario.Grupo;
import org.example.spring_ecommerce.model.usuario.Usuario;
import org.example.spring_ecommerce.model.usuario.UsuarioGrupo;
import org.example.spring_ecommerce.repositories.GrupoRepository;
import org.example.spring_ecommerce.repositories.UsuarioGrupoRepository;
import org.example.spring_ecommerce.repositories.UsuarioRepository;
import org.example.spring_ecommerce.configuration.security.jwt.JwtService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UsuarioService implements UserDetailsService {

    private final UsuarioRepository repository;
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
                        repository.save(usuario);
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

    //obtem o usuario com as permissoes
    public Usuario obterUsuarioComPermissoes(String email){
        Optional<Usuario> usuarioOptional = repository.findByEmail(email);
        if(usuarioOptional.isEmpty()){
            return null;
        }

        Usuario usuario = usuarioOptional.get();
        List<String> permissoes = usuarioGrupoRepository.findPermissoesByUsuario(usuario);
        usuario.setPermissoes(permissoes);

        return usuario;
    }

    //valida
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = repository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado na base de dados."));
        return new UsuarioDto(usuario, usuario.getPermissoes());
    }



    //envia token para alterar senha
    public void enviarSolicitacaoDeResetarSenha(String email) {
        Usuario user = repository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com email: " + email));

        String token = jwtService.gerarToken(user);

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

        Usuario user = repository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + email));

        if (usuarioAutenticado(user.getEmail())) {
            throw new IllegalStateException("Usuário está logado e não pode alterar a senha.");
        }

        user.setSenha(passwordEncoder.encode(newPassword));
        repository.save(user);
    }

    //verifica se o Usuario esta autenticado
    private boolean usuarioAutenticado(String email) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.isAuthenticated() && email.equals(authentication.getName());
    }
}
