package com.turmaa.helpdeskturmaa.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

/**
 * Classe utilitária para operações relacionadas a JSON Web Tokens (JWT).
 * Responsável por gerar, validar e extrair informações de tokens.
 */
@Component
public class JWTUtil {

    @Value("${jwt.expiration}")
    private Long expiration;

    @Value("${jwt.secret}")
    private String secret;

    /**
     * Gera um novo token JWT para o usuário.
     * Utiliza a sintaxe moderna da biblioteca jjwt.
     *
     * @param email O e-mail (username) do usuário.
     * @return uma String que representa o token JWT.
     */
    public String generateToken(String email) {
        // MÉTODO MODERNO: Gera uma chave segura a partir do seu segredo
        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes());
        
        return Jwts.builder()
                .setSubject(email)
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                // MÉTODO MODERNO: Assina com a chave gerada
                .signWith(key)
                .compact();
    }

    /**
     * Verifica se um token é válido.
     *
     * @param token O token a ser validado.
     * @return true se o token for válido, false caso contrário.
     */
    public boolean tokenValido(String token) {
        Claims claims = getClaims(token);
        if (claims != null) {
            String username = claims.getSubject();
            Date expirationDate = claims.getExpiration();
            Date now = new Date(System.currentTimeMillis());
            if (username != null && expirationDate != null && now.before(expirationDate)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Extrai o username (e-mail) de um token JWT.
     *
     * @param token O token do qual o username será extraído.
     * @return O e-mail do usuário contido no token.
     */
    public String getUsername(String token) {
        Claims claims = getClaims(token);
        if (claims != null) {
            return claims.getSubject();
        }
        return null;
    }

    /**
     * Extrai os "claims" de um token usando a sintaxe moderna.
     *
     * @param token O token a ser processado.
     * @return um objeto Claims com as informações do token, ou null se o token for inválido.
     */
    private Claims getClaims(String token) {
        try {
            SecretKey key = Keys.hmacShaKeyFor(secret.getBytes());
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        } catch (Exception e) {
            return null;
        }
    }
}

