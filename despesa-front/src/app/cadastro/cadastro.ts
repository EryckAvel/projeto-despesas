import { Component, inject } from '@angular/core';
import { FormsModule, NgModel, NgForm} from '@angular/forms';
import { CadastroLogin } from './cadastroLogin';
import { AuthService } from '../login/auth.service';
import { Router } from '@angular/router';

import { HlmFieldImports } from '@spartan-ng/helm/field';
import { HlmInputImports } from '@spartan-ng/helm/input';
import { HlmLabelImports } from '@spartan-ng/helm/label';
import { HlmButtonImports } from '@spartan-ng/helm/button';
import { toast } from '@spartan-ng/brain/sonner';
import { HttpErrorResponse } from '@angular/common/http';
import { HlmCardImports } from '@spartan-ng/helm/card';

import { NgIcon, provideIcons} from '@ng-icons/core';
import { lucideEye, lucideEyeOff } from '@ng-icons/lucide';

@Component({
  imports: [FormsModule, HlmCardImports,HlmFieldImports, HlmInputImports,HlmLabelImports, HlmButtonImports, NgIcon],
  providers: [provideIcons({ lucideEye, lucideEyeOff })],
  selector: 'app-cadastro',
  styleUrl: './cadastro.css',
  templateUrl: './cadastro.html',
})
export class Cadastro {

  cadastro: CadastroLogin = {nome:'', email: '', senha : ''};

  private authService = inject(AuthService);
  private router = inject(Router)

  cadastrar(form: NgForm) {
    if (form.invalid) {
      form.control.markAllAsTouched();
      toast.warning('Preencha todos os campos corretamente.', {
        duration: 2000
      });
      return;
    }
    this.authService.cadastro(this.cadastro).subscribe({
      next: (res) => {
        toast.success('Usuario cadastrado com sucesso!', {
          duration: 3000,
        });
        this.router.navigate(['/login'])
      },
      error: (err: HttpErrorResponse) => {
        toast.error(err.error?.message ?? 'Falha no Cadastro!', {
          duration: 3000,
        });
      }
    })
    console.log(this.cadastro);
  }

  invalido(campo: NgModel, form: NgForm): boolean {
    return !!campo.invalid && (!!campo.touched || form.submitted);
  }

}
