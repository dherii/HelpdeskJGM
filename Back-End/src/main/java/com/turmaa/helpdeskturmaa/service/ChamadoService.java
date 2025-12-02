package com.turmaa.helpdeskturmaa.service;

import com.turmaa.helpdeskturmaa.domain.Chamado;
import com.turmaa.helpdeskturmaa.domain.Cliente;
import com.turmaa.helpdeskturmaa.domain.Tecnico;
import com.turmaa.helpdeskturmaa.domain.dtos.ChamadoDTO;
import com.turmaa.helpdeskturmaa.domain.enums.Prioridade;
import com.turmaa.helpdeskturmaa.domain.enums.Status;
import com.turmaa.helpdeskturmaa.repositories.ChamadoRepository;
import com.turmaa.helpdeskturmaa.service.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ChamadoService {

    @Autowired
    private ChamadoRepository repository;
    @Autowired
    private TecnicoService tecnicoService;
    @Autowired
    private ClienteService clienteService;
    // A injeção do BCryptPasswordEncoder foi removida pois não é utilizada aqui.

    public Chamado findById(Integer id) {
        Optional<Chamado> obj = repository.findById(id);
        return obj.orElseThrow(() -> new ObjectNotFoundException("Objeto não encontrado! ID: " + id));
    }

    public List<Chamado> findAll() {
        return repository.findAll();
    }
    
    public Chamado create(ChamadoDTO dto) {
        return repository.save(newChamado(dto));
    }
    
    public Chamado update(Integer id, @Valid ChamadoDTO dto) {
        dto.setId(id);
        Chamado oldObj = findById(id);
        oldObj = newChamado(dto);
        return repository.save(oldObj);
    }
    
    private Chamado newChamado(ChamadoDTO dto) {
        Tecnico tecnico = tecnicoService.findById(dto.getTecnico());
        Cliente cliente = clienteService.findById(dto.getCliente());

        Chamado chamado = new Chamado();
        if(dto.getId() != null) {
            chamado.setId(dto.getId());
        }
        
        // Se o status for "ENCERRADO" (código 2), define a data de fechamento
        if(dto.getStatus().equals(2)) {
            chamado.setDataFechamento(LocalDate.now());
        }

        chamado.setTecnico(tecnico);
        chamado.setCliente(cliente);
        chamado.setPrioridade(Prioridade.toEnum(dto.getPrioridade()));
        chamado.setStatus(Status.toEnum(dto.getStatus()));
        chamado.setTitulo(dto.getTitulo());
        chamado.setObservacoes(dto.getObservacoes());
        return chamado;
    }
}

