package org.example.spring_ecommerce.configuration.security;

import lombok.RequiredArgsConstructor;
import org.example.spring_ecommerce.controllers.dto.UsuarioDto;
import org.example.spring_ecommerce.model.usuario.Usuario;
import org.example.spring_ecommerce.services.UsuarioService;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final UsuarioService usuarioService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) {

        String login = authentication.getName();
        String senha = (String) authentication.getCredentials();

        UsuarioDto usuario = usuarioService.obterUsuarioComPermissoes(login);

        if (usuario != null) {
            boolean senhasBatem = passwordEncoder.matches(senha, usuario.getPassword());

            if (senhasBatem) {
                UsuarioDto identificacaoUsuario = new UsuarioDto(usuario.getUsuario(), usuario.getPermissoes());

                return new CustomAuthentication(identificacaoUsuario);
            }
        }
        return authentication;
    }



    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
