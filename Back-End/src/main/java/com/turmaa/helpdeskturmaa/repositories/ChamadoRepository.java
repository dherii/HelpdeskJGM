package com.turmaa.helpdeskturmaa.repositories;

import com.turmaa.helpdeskturmaa.domain.Chamado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Interface de Repositório para a entidade Chamado.
 * * A anotação @Repository indica ao Spring que esta é uma classe de acesso a dados.
 * Ao estender JpaRepository<Chamado, Integer>, o Spring Data JPA automaticamente
 * fornece uma implementação com os métodos CRUD (Create, Read, Update, Delete)
 * para a entidade Chamado, que tem uma chave primária do tipo Integer.
 */
@Repository
public interface ChamadoRepository extends JpaRepository<Chamado, Integer> {

    // Neste momento, nenhum método customizado é necessário.
    // O JpaRepository já nos fornece tudo o que precisamos para começar, como:
    // - save(Chamado chamado)
    // - findById(Integer id)
    // - findAll()
    // - deleteById(Integer id)
    // - etc.

}
