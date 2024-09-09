package org.example.spring_ecommerce.controllers;

import lombok.RequiredArgsConstructor;

import org.example.spring_ecommerce.configuration.advices.exceptionExclusives.UsuarioNaoPodeCriarAdmin;

import org.example.spring_ecommerce.controllers.dto.UsuarioDto;
import org.example.spring_ecommerce.model.Venda;
import org.example.spring_ecommerce.model.usuario.Usuario;
import org.example.spring_ecommerce.configuration.security.jwt.JwtService;
import org.example.spring_ecommerce.services.UsuarioService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final JwtService jwtService;

    @PostMapping(path = "/user")
    public ResponseEntity<Usuario> salvarUsuario(@RequestBody UsuarioDto body) {
        if (body.getPermissoes().contains("ADMIN")) {
            throw new UsuarioNaoPodeCriarAdmin();
        }
        Usuario usuario = usuarioService.salvar(body.getUsuario(), body.getPermissoes());

        return ResponseEntity.ok(usuario);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(path = "/adm")
    public ResponseEntity<Usuario> salvarAdm(@RequestBody UsuarioDto body){
        Usuario usuarioSalvo = usuarioService.salvar(body.getUsuario(), body.getPermissoes());
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioSalvo);
    }

    @PostMapping("/autenticar")
            public ResponseEntity<String> autenticar(@RequestParam String email, @RequestParam String senha){
            Usuario usuario = Usuario.builder()
                    .email(email)
                    .senha(senha).build();

            usuarioService.autenticar(usuario);
            String token = jwtService.gerarToken(new UsuarioDto(usuario,usuario.getPermissoes()));
            return  ResponseEntity.status(HttpStatus.OK).body("Token gerado com sucesso:" + token);
 }

    @PostMapping("/resetar-senha-request")
    public ResponseEntity<String> requestPasswordReset(@RequestParam String email) {
        usuarioService.enviarSolicitacaoDeResetarSenha(email);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("Link de reset de senha enviado para o email.");
    }

    @PostMapping("/resetar-senha")
    public ResponseEntity<String> resetPassword(@RequestParam String token,
                                                @RequestParam String novaSenha) {
        usuarioService.resetarSenha(token, novaSenha);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("Senha alterada com sucesso.");
    }

    @PutMapping("depositar")
    public ResponseEntity<String> depositar(@RequestParam double valor) {
        usuarioService.depositar(valor);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body("Depositado com sucesso.");
    }

}
