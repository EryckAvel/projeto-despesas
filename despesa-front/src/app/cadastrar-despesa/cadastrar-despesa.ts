import { Component, inject, OnInit, signal } from '@angular/core';
import { FormsModule, NgForm, NgModel } from '@angular/forms';
import { DespesaCadastro } from './despesaCadastro';
import { DespesaService } from '../lista-despesas/despesa.service';
import { ActivatedRoute, Router } from '@angular/router';

import { HlmFieldImports } from '@spartan-ng/helm/field';
import { HlmInputImports } from '@spartan-ng/helm/input';
import { HlmLabelImports } from '@spartan-ng/helm/label';
import { HlmButtonImports } from '@spartan-ng/helm/button';
import { HlmSelectImports } from '@spartan-ng/helm/select';
import { toast } from '@spartan-ng/brain/sonner';
import { HttpErrorResponse } from '@angular/common/http';
import { HlmCardImports } from '@spartan-ng/helm/card';


@Component({
  imports: [FormsModule, HlmCardImports,HlmFieldImports, HlmInputImports,HlmLabelImports, HlmButtonImports, HlmSelectImports],
  selector: 'app-cadastrar-despesa',
  styleUrl: './cadastrar-despesa.css',
  templateUrl: './cadastrar-despesa.html',
})
export class CadastrarDespesa implements OnInit{

  id:number = 0;
  editando: boolean = false;
  categorias: string[] = ['ALIMENTACAO', 'TRANSPORTE', 'MORADIA', 'LAZER', 'SAUDE'];
  despesa = signal<DespesaCadastro>({
    descricao: '',
    valor: undefined,
    data: '',
    categoria: '',
  });

  private route = inject(ActivatedRoute)
  private router = inject(Router)
  private despesaService = inject(DespesaService);

  ngOnInit(): void {
    this.id = Number(this.route.snapshot.paramMap.get('id'));
    if (this.id) {
      this.editando = true;
      this.despesaService.buscarDespesa(this.id)
        .subscribe(d => this.despesa.set(d));
    }
  }

  salvar(form: NgForm){
    if (form.invalid) {
      form.control.markAllAsTouched();
      toast.warning('Preencha todos os campos corretamente.', {
        duration: 2000
      });
      return;
    }
    if(this.editando){
      this.atualizar();
    }else{
      this.cadastrar();
    }
  }

  cadastrar() {
    const payload = {
      ...this.despesa(),
      valor: Number(String(this.despesa().valor).replace(',', '.')),
    };
    this.despesaService.cadastrarDespesas(payload).subscribe({
      next: (res) => {
        toast.success('Despesa cadastrada com sucesso!', {
          duration: 3000,
        });
        this.router.navigate(['/despesa/listar'])
      },
      error: (err : HttpErrorResponse) => {
        toast.error(err.error?.message ?? 'Falha em cadastrar despesa!', {
          duration: 3000
        })
      }
    })
  }

  atualizar() {
    const payload = {
      ...this.despesa(),
      valor: Number(String(this.despesa().valor).replace(',', '.')),
    };
    this.despesaService.atualizarDespesa(payload, this.id).subscribe({
      next: (res) => {
        toast.success('Despesa atualizada com sucesso!', {
          duration: 3000,
        });
        this.router.navigate(['/despesa/listar'])
      },
      error: (err : HttpErrorResponse) => {          
        toast.error(err.error?.message ?? 'Falha em atualizar despesa!', {
          duration: 3000
        })
      }
    })
  }

  invalido(campo: NgModel, form: NgForm): boolean {
    return !!campo.invalid && (!!campo.touched || form.submitted);
  }

}
