package com.turmaa.helpdeskturmaa.controllers;

import com.turmaa.helpdeskturmaa.domain.Chamado;
import com.turmaa.helpdeskturmaa.domain.dtos.ChamadoDTO;
import com.turmaa.helpdeskturmaa.service.ChamadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/chamados")
public class ChamadoController {

    @Autowired
    private ChamadoService service;

    /**
     * Endpoint para buscar um chamado específico pelo seu ID.
     * Mapeado para GET /chamados/{id}
     */
    @GetMapping(value = "/{id}")
    public ResponseEntity<ChamadoDTO> findById(@PathVariable Integer id) {
        Chamado obj = service.findById(id);
        return ResponseEntity.ok().body(new ChamadoDTO(obj));
    }

    /**
     * Endpoint para listar todos os chamados cadastrados.
     * Mapeado para GET /chamados
     */
    @GetMapping
    public ResponseEntity<List<ChamadoDTO>> findAll() {
        List<Chamado> list = service.findAll();
        List<ChamadoDTO> listDTO = list.stream().map(ChamadoDTO::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(listDTO);
    }

    /**
     * Endpoint para criar um novo chamado.
     * Mapeado para POST /chamados
     */
    @PostMapping
    public ResponseEntity<ChamadoDTO> create(@Valid @RequestBody ChamadoDTO dto) {
        Chamado obj = service.create(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    /**
     * Endpoint para atualizar um chamado existente.
     * Mapeado para PUT /chamados/{id}
     */
    @PutMapping(value = "/{id}")
    public ResponseEntity<ChamadoDTO> update(@PathVariable Integer id, @Valid @RequestBody ChamadoDTO dto) {
        Chamado newObj = service.update(id, dto);
        return ResponseEntity.ok().body(new ChamadoDTO(newObj));
    }
}