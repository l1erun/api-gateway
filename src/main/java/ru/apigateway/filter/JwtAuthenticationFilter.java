package ru.apigateway.filter;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.server.authentication.*;
import org.springframework.stereotype.Component;
import org.springframework.web.server.*;

import reactor.core.publisher.Mono;

import java.util.*;

@Component
public class JwtAuthenticationFilter implements ServerAuthenticationConverter{
    @Value("${jwt.secret}")
    private String secretKey;

    @Override
    public Mono<Authentication> convert(ServerWebExchange exchange) {
        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String authToken = authHeader.substring(7);
            try {
                Claims claims = Jwts.parserBuilder()
                        .setSigningKey(secretKey)
                        .build()
                        .parseClaimsJws(authToken)
                        .getBody();

                String userId = claims.getSubject();
                List<String> roles = claims.get("roles", List.class);

                List<GrantedAuthority> authorities = new ArrayList<>();
                if (roles != null) {
                    for (String role : roles) {
                        authorities.add(new SimpleGrantedAuthority(role));
                    }
                }

                Authentication auth = new UsernamePasswordAuthenticationToken(userId, null, authorities);
                return Mono.just(auth);

            } catch (JwtException e) {
                return Mono.error(new BadCredentialsException("Invalid token"));
            }
        }

        return Mono.empty();
    }
}
