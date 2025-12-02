import { Component, OnInit } from '@angular/core';
import { FormControl, Validators, FormBuilder, FormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { Cliente } from 'src/app/models/cliente';
import { ClienteService } from 'src/app/core/services/cliente.service';

@Component({
  selector: 'app-cliente-create',
  templateUrl: './cliente-create.component.html',
  styleUrls: ['./cliente-create.component.css']
})
export class ClienteCreateComponent implements OnInit {

  cliente: Cliente = {
    id: '',
    nome: '',
    cpf: '',
    email: '',
    senha: '',
    perfis: [],
    dataCriacao: ''
  }

  form: FormGroup;

  constructor(
    private service: ClienteService,
    private toast: ToastrService,
    private router: Router,
    private fb: FormBuilder
  ) {
    this.form = this.fb.group({
      nome:  ['', [Validators.required, Validators.minLength(3)]],
      cpf:   ['', [Validators.required, Validators.minLength(11)]],
      email: ['', [Validators.required, Validators.email]],
      senha: ['', [Validators.required, Validators.minLength(3)]]
    });
  }

  ngOnInit(): void {
  }

  create(): void {
    this.cliente.nome = this.form.get('nome')?.value;
    this.cliente.cpf = this.form.get('cpf')?.value;
    this.cliente.email = this.form.get('email')?.value;
    this.cliente.senha = this.form.get('senha')?.value;
    
    // --- REGRA DE NEGÓCIO ---
    // Cliente sempre recebe perfil 1 (CLIENTE)
    this.cliente.perfis = [1]; 

    this.service.create(this.cliente).subscribe(
      resp => {
        this.toast.success('Cliente cadastrado com sucesso', 'Cadastro');
        this.router.navigate(['/clientes']);
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