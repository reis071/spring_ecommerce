package org.example.spring_ecommerce.configuration.errorGlobal.advices.expectionExclusives;

public class UsuarioNaoPodeCriarAdmin extends RuntimeException {

    public UsuarioNaoPodeCriarAdmin(String message) {
        super(message);
    }
}
