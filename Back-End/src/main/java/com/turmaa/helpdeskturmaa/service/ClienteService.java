package com.turmaa.helpdeskturmaa.service;

import com.turmaa.helpdeskturmaa.domain.Cliente;
import com.turmaa.helpdeskturmaa.domain.Pessoa;
import com.turmaa.helpdeskturmaa.domain.dtos.ClienteDTO;
import com.turmaa.helpdeskturmaa.repositories.ClienteRepository;
import com.turmaa.helpdeskturmaa.repositories.TecnicoRepository;
import com.turmaa.helpdeskturmaa.service.exceptions.DataIntegrityViolationException;
import com.turmaa.helpdeskturmaa.service.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository repository;
    @Autowired
    private TecnicoRepository tecnicoRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    
    public Cliente findById(Integer id) {
        Optional<Cliente> obj = repository.findById(id);
        return obj.orElseThrow(() -> new ObjectNotFoundException("Objeto não encontrado! Id: " + id));
    }

   public List<Cliente> findAll() {
        return repository.findAll();
    }

    public Cliente create(ClienteDTO dto) {
        dto.setId(null);
        dto.setSenha(passwordEncoder.encode(dto.getSenha()));
        validaPorCpfEEmail(dto);
        Cliente newObj = new Cliente(dto);
        return repository.save(newObj);
    } 

    public Cliente update(Integer id, ClienteDTO dto) {
        dto.setId(id);
        Cliente oldObj = findById(id);
        dto.setSenha(passwordEncoder.encode(dto.getSenha()));
        validaPorCpfEEmail(dto);
        oldObj = new Cliente(dto);
        return repository.save(oldObj);
    }

    public void delete(Integer id) {
        Cliente obj = findById(id);
        if (obj.getChamados().size() > 0) {
            throw new DataIntegrityViolationException("Cliente possui ordens de serviço e não pode ser deletado!");
        }
        repository.deleteById(id);
    }

    private void validaPorCpfEEmail(ClienteDTO dto) {
        Optional<Pessoa> obj = repository.findByCpf(dto.getCpf());
        if (obj.isPresent() && obj.get().getId() != dto.getId()) {
            throw new DataIntegrityViolationException("CPF já cadastrado no sistema!");
        }
        
        obj = tecnicoRepository.findByCpf(dto.getCpf());
        if (obj.isPresent() && obj.get().getId() != dto.getId()) {
            throw new DataIntegrityViolationException("CPF já cadastrado no sistema para um técnico!");
        }

        // Valida E-mail
        obj = repository.findByEmail(dto.getEmail());
        if (obj.isPresent() && obj.get().getId() != dto.getId()) {
            throw new DataIntegrityViolationException("E-mail já cadastrado no sistema!");
        }
        
        obj = tecnicoRepository.findByEmail(dto.getEmail());
        if (obj.isPresent() && obj.get().getId() != dto.getId()) {
            throw new DataIntegrityViolationException("E-mail já cadastrado no sistema para um técnico!");
        }
    }
}