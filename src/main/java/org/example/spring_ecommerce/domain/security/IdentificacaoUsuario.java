package org.example.spring_ecommerce.domain.security;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class IdentificacaoUsuario {
    private Long id;
    private String nome;
    private String email;
    private List<String> permissoes;

    public IdentificacaoUsuario(Long id, String nome, String email, List<String> permissoes) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.permissoes = permissoes;
    }

    public List<String> getPermissoes() {
        if(permissoes == null){
            permissoes = new ArrayList<>();
        }
        return permissoes;
    }
}
