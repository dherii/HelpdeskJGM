import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'prioridade'
})
export class PrioridadePipe implements PipeTransform {

  transform(value: any): string {
    if(value == 0) {
      return 'BAIXA';
    } else if(value == 1) {
      return 'MÉDIA';
    } else {
      return 'ALTA';
    }
  }
}