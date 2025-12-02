package com.turmaa.helpdeskturmaa.repositories;

import com.turmaa.helpdeskturmaa.domain.Pessoa;
import com.turmaa.helpdeskturmaa.domain.Tecnico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface TecnicoRepository extends JpaRepository<Tecnico, Integer> {
    Optional<Pessoa> findByCpf(String cpf);
    Optional<Pessoa> findByEmail(String email);
}