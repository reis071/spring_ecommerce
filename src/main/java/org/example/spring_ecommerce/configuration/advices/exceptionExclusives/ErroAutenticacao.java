package org.example.spring_ecommerce.configuration.advices.exceptionExclusives;

public class ErroAutenticacao extends RuntimeException{

    public ErroAutenticacao(){
        super("Email ou senha Incorreto");
    }
}
