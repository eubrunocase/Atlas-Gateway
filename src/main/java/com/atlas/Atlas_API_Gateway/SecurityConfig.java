package com.atlas.Atlas_API_Gateway;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Value("OUJWeHM3OUJMPi80SzQ7O0tsQWFNZHtYOmopYWVJVFYK")
    private String secret;


    @Bean
    public SecurityWebFilterChain securityWebFilterChain (ServerHttpSecurity http)throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers("/atlas/auth/login").permitAll()
                        // outros públicos, se houver
                        .pathMatchers("/atlas/auth/register").permitAll()

                        .anyExchange().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwtSpec -> jwtSpec
                                .jwtDecoder(jwtDecoder()))
                )
                .build();
    }

    @Bean
    public NimbusReactiveJwtDecoder jwtDecoder() {
        // converte o secret texto puro em bytes UTF-8
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        SecretKeySpec key = new SecretKeySpec(keyBytes, "HmacSHA256");
        return NimbusReactiveJwtDecoder.withSecretKey(key).build();
    }


}
