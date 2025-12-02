package com.turmaa.helpdeskturmaa.repositories;

import com.turmaa.helpdeskturmaa.domain.Pessoa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PessoaRepository extends JpaRepository<Pessoa, Integer> {

    /**
     * Busca uma Pessoa no banco de dados pelo seu endereço de e-mail.
     * O Spring Data JPA cria a implementação deste método automaticamente.
     *
     * @param email O e-mail a ser buscado.
     * @return um Optional contendo a Pessoa, se encontrada.
     */
    Optional<Pessoa> findByEmail(String email);
}
