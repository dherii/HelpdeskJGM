import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { Chamado } from 'src/app/models/chamado';
import { ChamadoService } from 'src/app/core/services/chamado.service';
import { FormBuilder, FormGroup } from '@angular/forms';

@Component({
  selector: 'app-chamado-read',
  templateUrl: './chamado-read.component.html',
  styleUrls: ['./chamado-read.component.css']
})
export class ChamadoReadComponent implements OnInit {

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

  // FormGroup vazio só para o HTML não quebrar, embora usemos [value] direto
  form: FormGroup;

  constructor(
    private service: ChamadoService,
    private route: ActivatedRoute,
    private toast: ToastrService,
    private fb: FormBuilder
  ) {
    this.form = this.fb.group({});
  }

  ngOnInit(): void {
    this.chamado.id = this.route.snapshot.paramMap.get('id');
    this.findById();
  }

  findById(): void {
    this.service.findById(this.chamado.id).subscribe(resposta => {
      this.chamado = resposta;
    }, ex => {
      this.toast.error(ex.error.error);
    })
  }

  retornaPrioridade(prioridade: any): string {
    if(prioridade == 0) return 'BAIXA';
    else if(prioridade == 1) return 'MÉDIA';
    else return 'ALTA';
  }

  retornaStatus(status: any): string {
    if(status == 0) return 'ABERTO';
    else if(status == 1) return 'ANDAMENTO';
    else return 'ENCERRADO';
  }
}