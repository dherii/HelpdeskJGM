import { Component, OnInit } from '@angular/core';
import { FormControl, Validators, FormBuilder, FormGroup } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { Tecnico } from 'src/app/models/tecnico';
import { TecnicoService } from 'src/app/core/services/tecnico.service';

@Component({
  selector: 'app-tecnico-update',
  templateUrl: './tecnico-update.component.html',
  styleUrls: ['./tecnico-update.component.css']
})
export class TecnicoUpdateComponent implements OnInit {

  tecnico: Tecnico = {
    id: '',
    nome: '',
    cpf: '',
    email: '',
    senha: '',
    perfis: [],
    dataCriacao: ''
  }

  perfis: number[] = []; // Array para controlar os checkboxes

  form: FormGroup;

  constructor(
    private service: TecnicoService,
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
    this.tecnico.id = this.route.snapshot.paramMap.get('id');
    this.findById();
  }

  findById(): void {
    this.service.findById(this.tecnico.id).subscribe(resposta => {
      this.tecnico = resposta;
      
      this.perfis = []; 

      // LÓGICA DE TRADUÇÃO (Java String -> Angular Number)
      if(this.tecnico.perfis) {
         this.tecnico.perfis.forEach(p => {
            let perfilCode: number | null = null;

            if (typeof p === 'string') {
                if (p === 'ADMIN') perfilCode = 0;
                else if (p === 'CLIENTE') perfilCode = 1;
                else if (p === 'TECNICO') perfilCode = 2;
            } else if (typeof p === 'number') {
                perfilCode = p;
            }

            if (perfilCode !== null) {
                this.perfis.push(perfilCode);
            }
         });
      }

      // Preenche o formulário
      this.form.patchValue({
        nome: this.tecnico.nome,
        cpf: this.tecnico.cpf,
        email: this.tecnico.email,
        senha: ''
      });
    });
  }

  update(): void {
    // 1. Validação básica do Front
    if (this.form.invalid) {
        this.toast.warning('Formulário inválido. Verifique os campos.');
        return;
    }

    this.tecnico.nome = this.form.get('nome')?.value;
    this.tecnico.cpf = this.form.get('cpf')?.value;
    this.tecnico.email = this.form.get('email')?.value;
    this.tecnico.senha = this.form.get('senha')?.value;
    
    // --- TRAVA DE SEGURANÇA (Regra de Negócio) ---
    // Garante que o perfil 2 (TÉCNICO) esteja na lista antes de salvar.
    // Isso protege caso algo tenha dado errado na manipulação visual.
    if(!this.perfis.includes(2)) {
      this.perfis.push(2);
    }

    // 2. Envia a lista de números (Integers)
    this.tecnico.perfis = this.perfis; 

    // 3. A Chamada ao Serviço
    this.service.update(this.tecnico).subscribe(
      resposta => {
        this.toast.success('Técnico atualizado com sucesso', 'Atualização');
        this.router.navigate(['/tecnicos']);
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
    );
  }

  addPerfil(perfil: number): void {
    if(this.perfis.includes(perfil)) {
      const index = this.perfis.indexOf(perfil);
      this.perfis.splice(index, 1);
    } else {
      this.perfis.push(perfil);
    }
  }
}