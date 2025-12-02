import { Component, OnInit } from '@angular/core';
import { FormControl, Validators, FormBuilder, FormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { Tecnico } from 'src/app/models/tecnico';
import { TecnicoService } from 'src/app/core/services/tecnico.service';

@Component({
  selector: 'app-tecnico-create',
  templateUrl: './tecnico-create.component.html',
  styleUrls: ['./tecnico-create.component.css']
})
export class TecnicoCreateComponent implements OnInit {

  // Objeto técnico inicial
  tecnico: Tecnico = {
    id: '',
    nome: '',
    cpf: '',
    email: '',
    senha: '',
    perfis: [],
    dataCriacao: ''
  }

  // Controle dos perfis selecionados (Assumindo: 0=ADMIN, 1=CLIENTE, 2=TECNICO)
  // Deixo o 2 pré-selecionado pois é um cadastro de TÉCNICO
  perfis: number[] = [2]; 

  form: FormGroup;

  constructor(
    private service: TecnicoService,
    private toast: ToastrService,
    private router: Router,
    private fb: FormBuilder
  ) {
    // Validações do Formulário
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
    // 1. Passa os valores do formulário
    this.tecnico.nome = this.form.get('nome')?.value;
    this.tecnico.cpf = this.form.get('cpf')?.value;
    this.tecnico.email = this.form.get('email')?.value;
    this.tecnico.senha = this.form.get('senha')?.value;
    
    // Regra de Negócio: Garantir que o perfil 2 (TÉCNICO) sempre seja enviado.
    // Mesmo que tenhamos inicializado com [2], isso é uma trava de segurança.
    if(!this.perfis.includes(2)) {
      this.perfis.push(2);
    }
    
    // 2. Envia a lista de números (Integers)
    this.tecnico.perfis = this.perfis;

    // 3. Chama o serviço
    this.service.create(this.tecnico).subscribe(
      resp => {
        this.toast.success('Técnico cadastrado com sucesso', 'Cadastro');
        this.router.navigate(['/tecnicos']);
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

  // Lógica para adicionar/remover perfil do array ao clicar no checkbox
  addPerfil(perfil: number): void {
    if(this.perfis.includes(perfil)) {
      // Se já tem, remove (desmarcou)
      const index = this.perfis.indexOf(perfil);
      this.perfis.splice(index, 1);
    } else {
      // Se não tem, adiciona (marcou)
      this.perfis.push(perfil);
    }
  }
}