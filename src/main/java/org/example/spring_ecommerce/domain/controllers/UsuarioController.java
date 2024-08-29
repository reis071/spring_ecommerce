package org.example.spring_ecommerce.domain.controllers;

import lombok.RequiredArgsConstructor;

import org.example.spring_ecommerce.domain.dto.UsuarioDto;
import org.example.spring_ecommerce.domain.entities.usuario.Usuario;
import org.example.spring_ecommerce.domain.security.jwt.JwtService;
import org.example.spring_ecommerce.domain.services.UsuarioService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.Path;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @PostMapping(path = "/add")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Usuario> salvar(@RequestBody UsuarioDto body){
        Usuario usuarioSalvo = usuarioService.salvar(body.getUsuario(), body.getPermissoes());
        return ResponseEntity.ok(usuarioSalvo);
    }

}
