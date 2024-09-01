package org.example.spring_ecommerce.configuration.advices.exceptionExclusives;

public class UsuarioNaoPodeCriarAdmin extends RuntimeException {

    public UsuarioNaoPodeCriarAdmin() {
        super("Usuario não tem permissão para criar Admin");
    }
}
