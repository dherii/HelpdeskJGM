package com.turmaa.helpdeskturmaa.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.turmaa.helpdeskturmaa.domain.dtos.CredenciaisDTO;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;
    private JWTUtil jwtUtil;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil) {
        super();
        setAuthenticationFailureHandler(new JWTAuthenticationFailureHandler());
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    /**
     * Tenta realizar a autenticação do usuário.
     * O bloco try-catch foi ajustado para permitir que o Spring Security lide
     * corretamente com as AuthenticationExceptions.
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        try {
            CredenciaisDTO creds = new ObjectMapper().readValue(request.getInputStream(), CredenciaisDTO.class);
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(creds.getEmail(),
                    creds.getSenha(), new ArrayList<>());
            
            // Agora, se a linha abaixo falhar, a exceção será tratada pelo FailureHandler.
            return authenticationManager.authenticate(authenticationToken);
        } catch (IOException e) {
            // Apenas o IOException é tratado, pois é um erro de leitura da requisição.
            throw new RuntimeException(e);
        }
    }

    /**
     * Chamado quando a autenticação é bem-sucedida.
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
            Authentication authResult) throws IOException, ServletException {
        
        String username = ((UserSS) authResult.getPrincipal()).getUsername();
        String token = jwtUtil.generateToken(username);
        response.setHeader("Authorization", "Bearer " + token);
        response.setHeader("access-control-expose-headers", "Authorization");
    }
}

