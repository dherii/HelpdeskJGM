package com.turmaa.helpdeskturmaa.service;

import com.turmaa.helpdeskturmaa.domain.Chamado;
import com.turmaa.helpdeskturmaa.domain.Cliente;
import com.turmaa.helpdeskturmaa.domain.Tecnico;
import com.turmaa.helpdeskturmaa.domain.enums.Perfil;
import com.turmaa.helpdeskturmaa.domain.enums.Prioridade;
import com.turmaa.helpdeskturmaa.domain.enums.Status;
import com.turmaa.helpdeskturmaa.repositories.ChamadoRepository;
import com.turmaa.helpdeskturmaa.repositories.ClienteRepository;
import com.turmaa.helpdeskturmaa.repositories.TecnicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class DBService {

    @Autowired
    private TecnicoRepository tecnicoRepository;
    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private ChamadoRepository chamadoRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public void instanciaDB() {
        // Técnico com perfil de ADMIN
        Tecnico tec1 = new Tecnico(null, "Bill Gates", "76845777093", "bill@mail.com", passwordEncoder.encode("123"));
        tec1.addPerfil(Perfil.ADMIN);

        // NOVO: Técnico com perfil apenas de TÉCNICO
        Tecnico tec2 = new Tecnico(null, "Richard Stallman", "10834340005", "stallman@mail.com", passwordEncoder.encode("456"));

        Cliente cli1 = new Cliente(null, "Linus Torvalds", "70511744013", "linus@mail.com", passwordEncoder.encode("123"));
        Cliente cli2 = new Cliente(null, "Steve Wozniak", "65329173050", "woz@mail.com", passwordEncoder.encode("456"));

        Chamado cha1 = new Chamado(null, Prioridade.MEDIA, Status.ANDAMENTO, "Chamado 01", "Primeiro chamado", tec1, cli1);
        Chamado cha2 = new Chamado(null, Prioridade.ALTA, Status.ABERTO, "Chamado 02", "Segundo chamado", tec2, cli2);

        tecnicoRepository.saveAll(Arrays.asList(tec1, tec2));
        clienteRepository.saveAll(Arrays.asList(cli1, cli2));
        chamadoRepository.saveAll(Arrays.asList(cha1, cha2));
    }
}

