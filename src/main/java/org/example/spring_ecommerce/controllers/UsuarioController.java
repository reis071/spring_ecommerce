package org.example.spring_ecommerce.controllers;

import lombok.RequiredArgsConstructor;

import org.example.spring_ecommerce.configuration.advices.exceptionExclusives.UsuarioNaoPodeCriarAdmin;
import org.example.spring_ecommerce.controllers.dto.CredenciaisDto;
import org.example.spring_ecommerce.controllers.dto.TokenDto;
import org.example.spring_ecommerce.controllers.dto.UsuarioDto;
import org.example.spring_ecommerce.model.Venda;
import org.example.spring_ecommerce.model.usuario.Usuario;
import org.example.spring_ecommerce.configuration.security.jwt.JwtService;
import org.example.spring_ecommerce.services.UsuarioService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final JwtService jwtService;

    @PostMapping(path = "/user")
    public ResponseEntity<String> salvarUsuario(@RequestBody UsuarioDto body) {
        if (body.getPermissoes().contains("ADMIN")) {
            throw new UsuarioNaoPodeCriarAdmin();
        }
        usuarioService.salvar(body.getUsuario(), body.getPermissoes());

        return ResponseEntity.ok("Usuario salvo com sucesso");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(path = "/adm")
    public ResponseEntity<Usuario> salvarAdm(@RequestBody UsuarioDto body){
        Usuario usuarioSalvo = usuarioService.salvar(body.getUsuario(), body.getPermissoes());
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioSalvo);
    }

    @PostMapping
    public ResponseEntity<TokenDto> autenticar(@RequestBody CredenciaisDto credenciais){
            Usuario usuario = Usuario.builder()
                    .email(credenciais.getEmail())
                    .senha(credenciais.getSenha()).build();

            usuarioService.autenticar(usuario);
            String token = jwtService.gerarToken(usuario);
            return  ResponseEntity.status(HttpStatus.OK).body(new TokenDto(usuario.getEmail(), token));
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
    public ResponseEntity<String> depositar(@RequestParam double valor,
                                            @RequestParam String email) {
        usuarioService.depositar(valor,email);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body("Depositado com sucesso.");
    }

    @PostMapping("comprar")
    public ResponseEntity<Venda> createVenda(@RequestParam String email,
                                             @RequestParam String nomeProduto,
                                             @RequestParam int quantidade) {

        Venda novaVenda = usuarioService.compra(email, nomeProduto, quantidade);
        return ResponseEntity.status(HttpStatus.CREATED).body(novaVenda);

    }
}
