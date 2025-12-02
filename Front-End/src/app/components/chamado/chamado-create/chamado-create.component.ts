import { Component, OnInit } from '@angular/core';
import { FormControl, Validators, FormBuilder, FormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { Chamado } from 'src/app/models/chamado';
import { Cliente } from 'src/app/models/cliente';
import { Tecnico } from 'src/app/models/tecnico';
import { ChamadoService } from 'src/app/core/services/chamado.service';
import { ClienteService } from 'src/app/core/services/cliente.service';
import { TecnicoService } from 'src/app/core/services/tecnico.service';

@Component({
  selector: 'app-chamado-create',
  templateUrl: './chamado-create.component.html',
  styleUrls: ['./chamado-create.component.css']
})
export class ChamadoCreateComponent implements OnInit {

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
    this.findAllClientes();
    this.findAllTecnicos();
  }

  // Busca lista para preencher o <mat-select>
  findAllClientes(): void {
    this.clienteService.findAll().subscribe(resposta => {
      this.clientes = resposta;
    })
  }

  findAllTecnicos(): void {
    this.tecnicoService.findAll().subscribe(resposta => {
      this.tecnicos = resposta;
    })
  }

  create(): void {
    this.chamado = this.form.value; // Pega tudo do form de uma vez

    this.chamadoService.create(this.chamado).subscribe(
      resp => {
        this.toast.success('Chamado criado com sucesso', 'Novo Chamado');
        this.router.navigate(['/chamados']);
      },
      ex => {
        console.log(ex);
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