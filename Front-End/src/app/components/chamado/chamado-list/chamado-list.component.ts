import { Component, OnInit, ViewChild } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatTableDataSource } from '@angular/material/table';
import { Chamado } from 'src/app/models/chamado';
import { ChamadoService } from 'src/app/core/services/chamado.service';

@Component({
  selector: 'app-chamado-list',
  templateUrl: './chamado-list.component.html',
  styleUrls: ['./chamado-list.component.css']
})
export class ChamadoListComponent implements OnInit {

  ELEMENT_DATA: Chamado[] = [];
  displayedColumns: string[] = ['id', 'titulo', 'cliente', 'dataAbertura', 'prioridade', 'status', 'acoes'];
  dataSource = new MatTableDataSource<Chamado>(this.ELEMENT_DATA);

  @ViewChild(MatPaginator) paginator!: MatPaginator;

  constructor(private service: ChamadoService) { }

  ngOnInit(): void {
    this.findAll();
  }

  findAll(): void {
    this.service.findAll().subscribe(resposta => {
      this.ELEMENT_DATA = resposta;
      this.dataSource = new MatTableDataSource<Chamado>(resposta);
      this.dataSource.paginator = this.paginator;
    });
  }

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();
  }

  // Retorna a classe CSS para colorir a etiqueta
  retornaClassePrioridade(prioridade: any): string {
    if(prioridade == 0) return 'prioridade-baixa';
    else if(prioridade == 1) return 'prioridade-media';
    else return 'prioridade-alta';
  }

  retornaClasseStatus(status: any): string {
    if(status == 0) return 'status-aberto';
    else if(status == 1) return 'status-andamento';
    else return 'status-encerrado';
  }

  orderByStatus(event: any): void {
    const status = event.value; // Pega o valor (0, 1, 2 ou 3)

    // Se for 3, resetamos para a lista completa
    if (status === 3) {
      this.dataSource = new MatTableDataSource<Chamado>(this.ELEMENT_DATA);
    } else {
      // Se não, filtramos a lista original
      const list: Chamado[] = [];
      this.ELEMENT_DATA.forEach(element => {
        if (element.status == status) {
          list.push(element);
        }
      });
      this.dataSource = new MatTableDataSource<Chamado>(list);
    }
    
    // Importante: Sempre que mudamos o dataSource, temos que reconectar o paginator
    this.dataSource.paginator = this.paginator;
  }
}