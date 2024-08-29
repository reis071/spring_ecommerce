package org.example.spring_ecommerce.domain.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.example.spring_ecommerce.domain.entities.usuario.Usuario;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
public class JwtService {
    /*
    @Value("${security.jwt.expiracao}")
    private String expiracao;

    @Value("${security.jwt.chave}")
    private String chave;

    public String gerarToken(Usuario usuario) {
        Long expStr = Long.valueOf(expiracao);
        LocalDateTime dataHoraExp = LocalDateTime.now().plusMinutes(expStr);
        Instant instant = dataHoraExp.atZone(ZoneId.systemDefault()).toInstant();
        Date data = Date.from(instant);
        return Jwts.builder()
                .setSubject(usuario.getEmail())
                .setExpiration(data)
                .signWith(SignatureAlgorithm.HS512,chave)
                .compact();
    }

    public Claims DecoToken(String token) throws ExpiredJwtException {
        return  Jwts.parser()
                .setSigningKey(chave)
                .parseClaimsJws(token)
                .getBody();
    }

    public Boolean tokenValido(String token) {
        try{
        Claims claims = DecoToken(token);
        Date dataExp= claims.getExpiration();
        LocalDateTime localDateTime = dataExp.
                toInstant().
                atZone(ZoneId.systemDefault()).toLocalDateTime();

        return !LocalDateTime.now().isAfter(localDateTime);
    }
    catch(ExpiredJwtException e){

        return false;}
    }

    public String obterEmail(String token) throws ExpiredJwtException {
       return (String) DecoToken(token).getSubject();
    }

     */
}
