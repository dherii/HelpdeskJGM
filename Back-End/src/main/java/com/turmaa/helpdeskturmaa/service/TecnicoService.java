package com.turmaa.helpdeskturmaa.service;

import com.turmaa.helpdeskturmaa.domain.Pessoa;
import com.turmaa.helpdeskturmaa.domain.Tecnico;
import com.turmaa.helpdeskturmaa.domain.dtos.TecnicoDTO;
import com.turmaa.helpdeskturmaa.repositories.ClienteRepository;
import com.turmaa.helpdeskturmaa.repositories.TecnicoRepository;
import com.turmaa.helpdeskturmaa.service.exceptions.DataIntegrityViolationException;
import com.turmaa.helpdeskturmaa.service.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder; // <- 1. IMPORT NECESSÁRIO
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;

@Service
public class TecnicoService {

    @Autowired
    private TecnicoRepository repository;
    @Autowired
    private ClienteRepository clienteRepository;
    
    // <- 2. INJEÇÃO DE DEPENDÊNCIA DO ENCODER DE SENHAS
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public Tecnico findById(Integer id) {
        Optional<Tecnico> obj = repository.findById(id);
        return obj.orElseThrow(() -> new ObjectNotFoundException("Objeto não encontrado! Id: " + id));
    }

    public List<Tecnico> findAll() {
        return repository.findAll();
    }

    public Tecnico create(TecnicoDTO dto) {
        dto.setId(null);
        // <- 3. CRIPTOGRAFA A SENHA ANTES DE SALVAR
        dto.setSenha(passwordEncoder.encode(dto.getSenha()));
        validaPorCpfEEmail(dto);
        Tecnico newObj = new Tecnico(dto);
        return repository.save(newObj);
    }

    public Tecnico update(Integer id, @Valid TecnicoDTO dto) {
        dto.setId(id);
        Tecnico oldObj = findById(id);
        // <- 4. CRIPTOGRAFA A SENHA TAMBÉM NO UPDATE
        dto.setSenha(passwordEncoder.encode(dto.getSenha()));
        validaPorCpfEEmail(dto);
        oldObj = new Tecnico(dto);
        return repository.save(oldObj);
    }

    public void delete(Integer id) {
        Tecnico obj = findById(id);
        if (obj.getChamados().size() > 0) {
            throw new DataIntegrityViolationException("Técnico possui ordens de serviço em aberto e não pôde ser deletado!");
        }
        repository.deleteById(id);
    }
    

    private void validaPorCpfEEmail(TecnicoDTO dto) {
        Optional<Pessoa> obj = repository.findByCpf(dto.getCpf());
        if (obj.isPresent() && obj.get().getId() != dto.getId()) {
            throw new DataIntegrityViolationException("CPF já cadastrado no sistema!");
        }
        
        obj = clienteRepository.findByCpf(dto.getCpf());
        if (obj.isPresent() && obj.get().getId() != dto.getId()) {
            throw new DataIntegrityViolationException("CPF já cadastrado no sistema para um cliente!");
        }

        obj = repository.findByEmail(dto.getEmail());
        if (obj.isPresent() && obj.get().getId() != dto.getId()) {
            throw new DataIntegrityViolationException("E-mail já cadastrado no sistema!");
        }
        
        obj = clienteRepository.findByEmail(dto.getEmail());
        if (obj.isPresent() && obj.get().getId() != dto.getId()) {
            throw new DataIntegrityViolationException("E-mail já cadastrado no sistema para um cliente!");
        }
    }
}

