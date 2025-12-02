package com.turmaa.helpdeskturmaa.repositories;

import com.turmaa.helpdeskturmaa.domain.Pessoa;
import com.turmaa.helpdeskturmaa.domain.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Integer> {
    Optional<Pessoa> findByCpf(String cpf);
    Optional<Pessoa> findByEmail(String email);
}