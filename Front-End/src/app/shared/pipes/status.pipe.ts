import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'status'
})
export class StatusPipe implements PipeTransform {

  transform(value: any): string {
    if(value == 0) {
      return 'ABERTO';
    } else if(value == 1) {
      return 'ANDAMENTO';
    } else {
      return 'ENCERRADO';
    }
  }
}