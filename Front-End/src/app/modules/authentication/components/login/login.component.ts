import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';

import { AuthService } from 'src/app/core/services/auth.service';
import { ToastrService } from 'ngx-toastr';
import { Credenciais } from 'src/app/models/credenciais';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  loading = false;
  hide = true;
  form: FormGroup;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router,
    private toast: ToastrService
  ) {
    this.form = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      senha: ['', [Validators.required, Validators.minLength(3)]]
    });
  }

  ngOnInit(): void {
  }

  login() {
    if (this.form.invalid) {
      this.validaCampos();
      return;
    }

    this.loading = true;

    const creds: Credenciais = {
      email: this.form.get('email')?.value,
      senha: this.form.get('senha')?.value
    }

    this.authService.authenticate(creds).subscribe(
      (resposta: any) => {
        this.loading = false;
        
        const token = resposta.headers.get('Authorization').substring(7);
        this.authService.successfulLogin(token);
        
        this.router.navigate(['']);
        this.toast.success('Bem-vindo ao sistema!', 'Login realizado');
      },
      (error) => {
        this.loading = false;
        this.toast.error('Usuário e/ou senha inválidos');
        console.error(error);
      }
    );
  }


  validaCampos(): void {
    const controls = this.form.controls;
    for (const name in controls) {
      if (controls[name]) {
        controls[name].markAsTouched();
      }
    }
    // Opcional: Mostrar um aviso extra para o usuário preencher tudo
    // this.toast.info('Preencha todos os campos obrigatórios');
  }
}