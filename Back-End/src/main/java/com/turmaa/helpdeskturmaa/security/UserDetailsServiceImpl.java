package com.turmaa.helpdeskturmaa.security;

import com.turmaa.helpdeskturmaa.domain.Pessoa;
import com.turmaa.helpdeskturmaa.repositories.PessoaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private PessoaRepository repository;

    /**
     * Método que o Spring Security utiliza para carregar um usuário pelo seu username (neste caso, o e-mail).
     *
     * @param email O e-mail do usuário que está tentando se autenticar.
     * @return um UserDetails (nossa classe UserSS) com os dados do usuário.
     * @throws UsernameNotFoundException se o usuário não for encontrado no banco de dados.
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Pessoa> pessoa = repository.findByEmail(email);

        if (pessoa.isPresent()) {
            return new UserSS(pessoa.get().getId(), pessoa.get().getEmail(), pessoa.get().getSenha(), pessoa.get().getPerfis());
        }
        throw new UsernameNotFoundException("Usuário não encontrado: " + email);
    }
}
