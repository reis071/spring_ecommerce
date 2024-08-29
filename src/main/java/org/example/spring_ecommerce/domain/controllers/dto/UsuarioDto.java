package org.example.spring_ecommerce.domain.controllers.dto;

import lombok.Data;
import org.example.spring_ecommerce.domain.entities.usuario.Usuario;

import java.util.List;

@Data
public class UsuarioDto {

    private Usuario usuario;

    private List<String> permissoes;
}
