import { Component, inject, OnInit, signal } from '@angular/core';
import { Despesa } from './despesa';
import { DespesaService } from './despesa.service';
import { Router } from '@angular/router';
import { CurrencyPipe, DatePipe } from '@angular/common';

import { HlmTableImports } from '@spartan-ng/helm/table';
import { HlmAlertDialogImports } from '@spartan-ng/helm/alert-dialog';

import { NgIcon, provideIcons } from '@ng-icons/core';
import { lucidePencil, lucidePlus, lucideTrash2 } from '@ng-icons/lucide';

@Component({
  imports: [CurrencyPipe, DatePipe, HlmTableImports, NgIcon, HlmAlertDialogImports],
  providers: [provideIcons({ lucidePlus, lucidePencil, lucideTrash2 })],
  selector: 'app-lista-despesas',
  styleUrl: './lista-despesas.css',
  templateUrl: './lista-despesas.html',
})
export class ListaDespesas implements OnInit{

  despesas= signal<Despesa[]>([]);

  private router = inject(Router);
  private despesaService = inject(DespesaService);

  nomesCategoria: Record<string, string> = {
    ALIMENTACAO: 'Alimentação',
    MORADIA: 'Moradia',
    TRANSPORTE: 'Transporte',
    LAZER: 'Lazer',
  };

  ngOnInit(): void {
    this.despesaService.listarDespesas().subscribe({
      next: (res) => this.despesas.set(res),
      error: (err) => console.error(err)
    })
  }

  cadastrar(){
    this.router.navigate(['/despesa/cadastro']);
  }

  atualizar(iddespesa: number){
    this.router.navigate(['/despesa', iddespesa, 'editar']);
  }
  
  excluir(iddespesa: number) {
    this.despesaService.deletarDespesa(iddespesa).subscribe({
      next: () => {
        this.despesaService.listarDespesas().subscribe({
          next: (res) => this.despesas.set(res),
          error: (err) => console.error(err)
        })  
      },
      error: (err) => console.error(err)
    })
  }

}
