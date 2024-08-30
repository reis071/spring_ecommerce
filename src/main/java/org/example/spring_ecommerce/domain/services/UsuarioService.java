package org.example.spring_ecommerce.domain.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.spring_ecommerce.domain.entities.Email;
import org.example.spring_ecommerce.domain.entities.usuario.Grupo;
import org.example.spring_ecommerce.domain.entities.usuario.Usuario;
import org.example.spring_ecommerce.domain.entities.usuario.UsuarioGrupo;
import org.example.spring_ecommerce.domain.repositories.GrupoRepository;
import org.example.spring_ecommerce.domain.repositories.UsuarioGrupoRepository;
import org.example.spring_ecommerce.domain.repositories.UsuarioRepository;
import org.example.spring_ecommerce.domain.security.jwt.JwtService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
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
    private final EmailService emailService; // Corrigido: Adicione a dependência final e construtor
    private final JwtService jwtService;

    @Transactional
    public Usuario salvar(Usuario usuario, List<String> grupos){
        String senhaCriptografada = passwordEncoder.encode(usuario.getSenha());
        usuario.setSenha(senhaCriptografada);
        repository.save(usuario);

        List<UsuarioGrupo> listaUsuarioGrupo = grupos.stream().map(nomeGrupo -> {
                    Optional<Grupo> possivelGrupo = grupoRepository.findByNome(nomeGrupo);
                    if (possivelGrupo.isPresent()) {
                        Grupo grupo = possivelGrupo.get();
                        return new UsuarioGrupo(usuario, grupo);
                    }
                    return null;
                })
                .filter(grupo -> grupo != null)
                .collect(Collectors.toList());

        usuarioGrupoRepository.saveAll(listaUsuarioGrupo);

        return usuario;
    }

    public UserDetails autenticar( Usuario usuario ){
        UserDetails user = loadUserByUsername(usuario.getEmail());
        boolean senhasBatem = passwordEncoder.matches( usuario.getSenha(), user.getPassword() );
        if(senhasBatem){
            return user;
        }
        return null;
    }

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

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = repository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado na base de dados."));
        return usuario;
    }

    public List<Usuario> obterTodosUsuarios(){
        return repository.findAll();
    }

    public void sendPasswordResetEmail(String email) {
        Usuario user = repository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com email: " + email));

        String token = jwtService.gerarToken(user);

        Email emailDetails = new Email(
                user.getEmail(),
                "Reset de Senha",
                "Para redefinir sua senha, clique no link abaixo:\n"
                        + "http://localhost:8080/reset-password?token=" + token
        );

        emailService.sendEmail(emailDetails); // Corrigido: Método correto
    }

    public void resetPassword(String token, String newPassword) {
        if (!jwtService.tokenValido(token)) {
            throw new IllegalStateException("Token inválido ou expirado");
        }

        String email = jwtService.obterLoginUsuario(token);

        Usuario user = repository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + email));

        if (isUserAuthenticated(user.getEmail())) {
            throw new IllegalStateException("Usuário está logado e não pode alterar a senha.");
        }

        user.setSenha(passwordEncoder.encode(newPassword));
        repository.save(user);
    }

    private boolean isUserAuthenticated(String email) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.isAuthenticated() && email.equals(authentication.getName());
    }
}
