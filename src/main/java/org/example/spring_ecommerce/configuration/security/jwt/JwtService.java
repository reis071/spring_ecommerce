package org.example.spring_ecommerce.configuration.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.example.spring_ecommerce.controllers.dto.UsuarioDto;
import org.example.spring_ecommerce.model.usuario.Usuario;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
public class JwtService {

    @Value("${security.jwt.expiracao}")
    private String expiracao;

    @Value("${security.jwt.chave}")
    private String chave;

    public String gerarToken(UsuarioDto usuarioDto) {
        long expString = Long.valueOf(expiracao);
        LocalDateTime dataHoraExpiracao = LocalDateTime.now().plusMinutes(expString);
        Instant instant = dataHoraExpiracao.atZone(ZoneId.systemDefault()).toInstant();
        Date data = Date.from(instant);

        return Jwts.builder()
                .setSubject(usuarioDto.getUsername())
                .claim("roles", usuarioDto.getPermissoes()) // Usa permissões do UsuarioDto
                .setExpiration(data)
                .signWith(SignatureAlgorithm.HS512, chave)
                .compact();
    }


    private Claims obterClaims( String token ) throws ExpiredJwtException {
        return Jwts
                .parser()
                .setSigningKey(chave)
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean tokenValido( String token ){
        try{
            Claims claims = obterClaims(token);
            Date dataExpiracao = claims.getExpiration();
            LocalDateTime data =
                    dataExpiracao.toInstant()
                            .atZone(ZoneId.systemDefault()).toLocalDateTime();
            return !LocalDateTime.now().isAfter(data);
        }catch (Exception e){
            return false;
        }
    }

    public String obterLoginUsuario(String token) throws ExpiredJwtException{
        return (String) obterClaims(token).getSubject();
    }


}
