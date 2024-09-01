package org.example.spring_ecommerce.configuration.security;

import org.example.spring_ecommerce.configuration.security.jwt.JwtAuthFilter;
import org.example.spring_ecommerce.services.UsuarioService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.core.GrantedAuthorityDefaults;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   ValidationPass validationPass,
                                                   JwtAuthFilter jwtAuthFilter,
                                                   CustomAuthenticationProvider customAuthenticationProvider,
                                                   UsuarioService usuarioService) throws Exception {
        return  http.
        csrf(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(customizer -> {
                    customizer.requestMatchers("/usuarios/resetar-senha-request").permitAll();
                    customizer.requestMatchers("/usuarios/resetar-senha").permitAll();
                    customizer.anyRequest().authenticated();
                })
                .httpBasic(Customizer.withDefaults())
                .authenticationProvider(customAuthenticationProvider)
                .authenticationProvider(validationPass)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }



    @Bean
    public GrantedAuthorityDefaults grantedAuthorityDefaults() {
        return new GrantedAuthorityDefaults("");
    }
}
