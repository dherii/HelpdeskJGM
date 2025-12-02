package com.turmaa.helpdeskturmaa.security;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

    private JWTUtil jwtUtil;
    private UserDetailsService userDetailsService;

    public JWTAuthorizationFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil, UserDetailsService userDetailsService) {
        super(authenticationManager);
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    /**
     * Este método é executado para cada requisição. Ele intercepta a requisição,
     * valida o token e define o contexto de autenticação do Spring.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        // 1. Pega o valor do cabeçalho "Authorization"
        String header = request.getHeader("Authorization");
        
        // 2. Verifica se o cabeçalho existe e se começa com "Bearer "
        if (header != null && header.startsWith("Bearer ")) {
            // 3. Obtém o token de autenticação (sem o "Bearer ")
            System.out.println(">>> [JWTAuthorizationFilter] Header 'Authorization' encontrado.");

            String token = header.substring(7);

            if (jwtUtil.tokenValido(token)) {
                System.out.println(">>> [JWTAuthorizationFilter] Token é válido.");
                
                // Obtém o objeto de autenticação a partir do token
                UsernamePasswordAuthenticationToken auth = getAuthentication(token);
                
                if (auth != null) {
                    System.out.println(">>> [JWTAuthorizationFilter] Autenticação criada com sucesso. Setando no contexto de segurança.");
                    SecurityContextHolder.getContext().setAuthentication(auth);
                } else {
                    System.out.println(">>> [JWTAuthorizationFilter] FALHA: Não foi possível criar a autenticação.");
                }
            } else {
                 System.out.println(">>> [JWTAuthorizationFilter] ATENÇÃO: Token inválido recebido.");
            }
        } else {
            System.out.println(">>> [JWTAuthorizationFilter] Header 'Authorization' não encontrado ou mal formatado.");
        }
        
        chain.doFilter(request, response);
        System.out.println(">>> [JWTAuthorizationFilter] Finalizando filtro.\n");
    }

    private UsernamePasswordAuthenticationToken getAuthentication(String token) {
        String username = jwtUtil.getUsername(token);
        System.out.println(">>> [JWTAuthorizationFilter] Username extraído do token: " + username);
        
        if (username == null) {
            System.out.println(">>> [JWTAuthorizationFilter] FALHA: Username não encontrado no token.");
            return null;
        }

        // Busca o usuário completo no banco de dados para pegar as permissões atualizadas
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        
        System.out.println(">>> [JWTAuthorizationFilter] Detalhes do usuário carregados do banco. Verificando permissões (authorities):");
        if (userDetails.getAuthorities() == null || userDetails.getAuthorities().isEmpty()) {
            System.out.println(">>> [JWTAuthorizationFilter] ATENÇÃO: NENHUMA PERMISSÃO (AUTHORITY) FOI ENCONTRADA PARA ESTE USUÁRIO!");
        } else {
            userDetails.getAuthorities().forEach(authority -> {
                System.out.println("    - Authority encontrada: " + authority.getAuthority());
            });
        }
        
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}
