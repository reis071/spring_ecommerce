package org.example.spring_ecommerce.domain.services;

import org.example.spring_ecommerce.domain.entities.Usuario;
import org.example.spring_ecommerce.domain.repositories.UsuarioRepository;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    private UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public Usuario save(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }


}
