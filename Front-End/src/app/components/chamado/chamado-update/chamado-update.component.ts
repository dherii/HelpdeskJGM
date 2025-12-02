import { Component, OnInit } from '@angular/core';
import { FormControl, Validators, FormBuilder, FormGroup } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { Chamado } from 'src/app/models/chamado';
import { Cliente } from 'src/app/models/cliente';
import { Tecnico } from 'src/app/models/tecnico';
import { ChamadoService } from 'src/app/core/services/chamado.service';
import { ClienteService } from 'src/app/core/services/cliente.service';
import { TecnicoService } from 'src/app/core/services/tecnico.service';

@Component({
  selector: 'app-chamado-update',
  templateUrl: './chamado-update.component.html',
  styleUrls: ['./chamado-update.component.css']
})
export class ChamadoUpdateComponent implements OnInit {

  chamado: Chamado = {
    prioridade: '',
    status: '',
    titulo: '',
    observacoes: '',
    tecnico: '',
    cliente: '',
    nomeCliente: '',
    nomeTecnico: ''
  }

  clientes: Cliente[] = [];
  tecnicos: Tecnico[] = [];

  form: FormGroup;

  constructor(
    private chamadoService: ChamadoService,
    private clienteService: ClienteService,
    private tecnicoService: TecnicoService,
    private toast: ToastrService,
    private router: Router,
    private route: ActivatedRoute,
    private fb: FormBuilder
  ) {
    this.form = this.fb.group({
      titulo:      ['', [Validators.required]],
      prioridade:  ['', [Validators.required]],
      status:      ['', [Validators.required]],
      tecnico:     ['', [Validators.required]],
      cliente:     ['', [Validators.required]],
      observacoes: ['', [Validators.required]]
    });
  }

  ngOnInit(): void {
    this.chamado.id = this.route.snapshot.paramMap.get('id');
    this.findById();
    this.findAllClientes();
    this.findAllTecnicos();
  }

  findById(): void {
    this.chamadoService.findById(this.chamado.id).subscribe(resposta => {
      this.chamado = resposta;
      
      // Preenche o formulário
      this.form.patchValue({
        titulo: this.chamado.titulo,
        prioridade: this.chamado.prioridade,
        status: this.chamado.status,
        tecnico: this.chamado.tecnico,
        cliente: this.chamado.cliente,
        observacoes: this.chamado.observacoes
      });
    }, ex => {
      this.toast.error(ex.error.error);
    })
  }

  // Listas para os Selects
  findAllClientes(): void {
    this.clienteService.findAll().subscribe(resposta => this.clientes = resposta);
  }

  findAllTecnicos(): void {
    this.tecnicoService.findAll().subscribe(resposta => this.tecnicos = resposta);
  }

  update(): void {
    this.chamado = this.form.value;
    // Recupera o ID que estava na URL, pois o form.value não tem ID
    this.chamado.id = this.route.snapshot.paramMap.get('id');

    this.chamadoService.update(this.chamado).subscribe(
      resp => {
        this.toast.success('Chamado atualizado com sucesso', 'Atualizar Chamado');
        this.router.navigate(['/chamados']);
      },
      ex => {
        if(ex.error.errors) {
          ex.error.errors.forEach((element: any) => {
            this.toast.error(element.message);
          });
        } else {
          this.toast.error(ex.error.message);
        }
      }
    )
  }
}