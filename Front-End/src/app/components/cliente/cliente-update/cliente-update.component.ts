import { Component, OnInit } from '@angular/core';
import { FormControl, Validators, FormBuilder, FormGroup } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { Cliente } from 'src/app/models/cliente';
import { ClienteService } from 'src/app/core/services/cliente.service';

@Component({
  selector: 'app-cliente-update',
  templateUrl: './cliente-update.component.html',
  styleUrls: ['./cliente-update.component.css']
})
export class ClienteUpdateComponent implements OnInit {

  cliente: Cliente = {
    id: '', nome: '', cpf: '', email: '', senha: '',
    perfis: [], dataCriacao: ''
  }

  form: FormGroup;

  constructor(
    private service: ClienteService,
    private toast: ToastrService,
    private router: Router,
    private route: ActivatedRoute,
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
    this.cliente.id = this.route.snapshot.paramMap.get('id');
    this.findById();
  }

  findById(): void {
    // 1. Verifica se o ID realmente chegou
    console.log("--> ID Buscado:", this.cliente.id);

    this.service.findById(this.cliente.id).subscribe(
      // SUCESSO
      resposta => {
        console.log("--> Resposta do Backend:", resposta); 
        
        this.cliente = resposta;
        
        // Preenche o formulário
        this.form.patchValue({
          nome: this.cliente.nome,
          cpf: this.cliente.cpf,
          email: this.cliente.email,
          senha: '' 
        });
      },
      ex => {
        console.error("--> ERRO AO BUSCAR:", ex);
        this.toast.error("Erro ao buscar cliente. Verifique o console.");
      }
    );
  }

  update(): void {
    this.cliente.nome = this.form.get('nome')?.value;
    this.cliente.cpf = this.form.get('cpf')?.value;
    this.cliente.email = this.form.get('email')?.value;
    this.cliente.senha = this.form.get('senha')?.value;
    
    // REGRA DE OURO: Garante que continua sendo Cliente (Perfil 1)
    this.cliente.perfis = [1]; 

    this.service.update(this.cliente).subscribe(
      () => {
        this.toast.success('Cliente atualizado com sucesso', 'Atualização');
        this.router.navigate(['/clientes']);
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