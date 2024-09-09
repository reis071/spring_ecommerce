package org.example.spring_ecommerce.configuration.security;

import org.example.spring_ecommerce.controllers.dto.UsuarioDto;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.stream.Collectors;

public class CustomAuthentication implements Authentication {

    private final UsuarioDto identificacaoUsuario;


    public CustomAuthentication(UsuarioDto identificacaoUsuario) {
        if(identificacaoUsuario == null){
            throw new ExceptionInInitializerError("Não é possível criar um customauthentication sem a identificacao do usuario");
        }
        this.identificacaoUsuario = identificacaoUsuario;

    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.identificacaoUsuario
                .getPermissoes()
                .stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getDetails() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return this.identificacaoUsuario;
    }

    @Override
    public boolean isAuthenticated() {
        return true;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        throw new IllegalArgumentException("Nao precisa chamar, já estamos autenticados");
    }

    @Override
    public String getName() {
        return this.identificacaoUsuario.getUsername();
    }
}
