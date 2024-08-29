package org.example.spring_ecommerce.domain.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.spring_ecommerce.domain.entities.usuario.Grupo;
import org.example.spring_ecommerce.domain.entities.usuario.Usuario;
import org.example.spring_ecommerce.domain.entities.usuario.UsuarioGrupo;
import org.example.spring_ecommerce.domain.repositories.GrupoRepository;
import org.example.spring_ecommerce.domain.repositories.UsuarioGrupoRepository;
import org.example.spring_ecommerce.domain.repositories.UsuarioRepository;
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
}
