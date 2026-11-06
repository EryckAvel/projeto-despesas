import { Component, inject } from '@angular/core';

import { HlmFieldImports } from '@spartan-ng/helm/field';
import { HlmInputImports } from '@spartan-ng/helm/input';
import { HlmCardImports } from '@spartan-ng/helm/card';
import { HlmLabelImports } from '@spartan-ng/helm/label';
import { HlmButtonImports } from '@spartan-ng/helm/button';
import { toast } from '@spartan-ng/brain/sonner';
import { FormsModule, NgForm } from '@angular/forms';
import { ResetSenhaRequest } from './resetSenhaRequest';
import { AuthService } from '../login/auth.service';
import { Router } from '@angular/router';
import { HttpErrorResponse} from '@angular/common/http';


import { NgIcon, provideIcons} from '@ng-icons/core';
import { lucideEye, lucideEyeOff } from '@ng-icons/lucide';

@Component({
  imports: [FormsModule, HlmCardImports, HlmFieldImports, HlmInputImports, HlmLabelImports, HlmButtonImports, NgIcon],
  providers: [provideIcons({ lucideEye, lucideEyeOff })],
  selector: 'app-reset-senha',
  styleUrl: './reset-senha.css',
  templateUrl: './reset-senha.html',
})
export class ResetSenha {

  resetRequest: ResetSenhaRequest = {email: undefined, novaSenha : undefined};

  private router = inject(Router);
  private authService = inject(AuthService)

  alterarSenha(form: NgForm){
    if (form.invalid) {
      form.control.markAllAsTouched();
      toast.warning('Preencha todos os campos corretamente.', {
        duration: 2000
      });
      return; // não chama a API
    }
    this.authService.alterarSenha(this.resetRequest).subscribe({
      next: (res) => {
        toast.success('Senha alterada com sucesso!', {
          duration: 3000,
        });
        this.router.navigate(['/login'])
      },
      error: (err: HttpErrorResponse) => {
        toast.error(err.error?.message ?? 'Falha ao realizar Login!', {
          duration: 3000
        })
      }
    })
  }

}
