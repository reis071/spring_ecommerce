package org.example.spring_ecommerce.configuration.advices.exceptionExclusives;

public class TokenInvalido extends RuntimeException{

    public TokenInvalido(){
        super("Token invalido");
    }
}
