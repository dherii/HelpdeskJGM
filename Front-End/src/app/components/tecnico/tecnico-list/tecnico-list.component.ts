import { Component, OnInit, ViewChild } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatTableDataSource } from '@angular/material/table';
import { Tecnico } from 'src/app/models/tecnico';
import { TecnicoService } from 'src/app/core/services/tecnico.service';

@Component({
  selector: 'app-tecnico-list',
  templateUrl: './tecnico-list.component.html',
  styleUrls: ['./tecnico-list.component.css']
})
export class TecnicoListComponent implements OnInit {

  ELEMENT_DATA: Tecnico[] = [];
  
  // As colunas que serão exibidas (tem que bater com o HTML)
  displayedColumns: string[] = ['id', 'nome', 'cpf', 'email', 'acoes'];
  
  // Fonte de dados específica do Material Table
  dataSource = new MatTableDataSource<Tecnico>(this.ELEMENT_DATA);

  // Pega a referência do paginator do HTML
  @ViewChild(MatPaginator) paginator!: MatPaginator;

  constructor(private service: TecnicoService) { }

  ngOnInit(): void {
    this.findAll();
  }

  findAll() {
    this.service.findAll().subscribe(resposta => {
      this.ELEMENT_DATA = resposta;
      // Atualiza o dataSource com os dados que vieram do Back
      this.dataSource = new MatTableDataSource<Tecnico>(resposta);
      // Vincula o paginator aos dados carregados
      this.dataSource.paginator = this.paginator;
    });
  }

  // Função padrão do Material para filtro
  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();
  }
}