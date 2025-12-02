import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { Tecnico } from 'src/app/models/tecnico';
import { TecnicoService } from 'src/app/core/services/tecnico.service';

@Component({
  selector: 'app-tecnico-delete',
  templateUrl: './tecnico-delete.component.html',
  styleUrls: ['./tecnico-delete.component.css']
})
export class TecnicoDeleteComponent implements OnInit {

  tecnico: Tecnico = {
    id: '',
    nome: '',
    cpf: '',
    email: '',
    senha: '',
    perfis: [],
    dataCriacao: ''
  }

  perfis: number[] = []; 

  constructor(
    private service: TecnicoService,
    private toast: ToastrService,
    private router: Router,
    private route: ActivatedRoute
  ) { }

  ngOnInit(): void {
    this.tecnico.id = this.route.snapshot.paramMap.get('id');
    this.findById();
  }

  findById(): void {
    this.service.findById(this.tecnico.id).subscribe(resposta => {
      this.tecnico = resposta;
      
      this.perfis = [];
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
    });
  }

  delete(): void {
    this.service.delete(this.tecnico.id).subscribe(
      () => {
        this.toast.success('Técnico removido com sucesso', 'Delete');
        this.router.navigate(['/tecnicos']);
      },
      ex => {
        // Tratamento importante: Se o técnico tiver chamados abertos,
        // o banco de dados NÃO vai deixar deletar (Integridade Referencial).
        // Esse erro vai aparecer aqui.
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