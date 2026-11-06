import { Component, inject } from '@angular/core';
import { LoginDados } from './loginDados';
import { FormsModule, NgForm, NgModel } from '@angular/forms';
import { AuthService } from './auth.service';
import { Router, RouterLink } from '@angular/router';
import { TokenService } from './token.service';

import { HlmFieldImports } from '@spartan-ng/helm/field';
import { HlmInputImports } from '@spartan-ng/helm/input';
import { HlmCardImports } from '@spartan-ng/helm/card';
import { HlmLabelImports } from '@spartan-ng/helm/label';
import { HlmButtonImports } from '@spartan-ng/helm/button';
import { toast } from '@spartan-ng/brain/sonner';

import { NgIcon, provideIcons} from '@ng-icons/core';
import { lucideEye, lucideEyeOff } from '@ng-icons/lucide';

@Component({
  imports: [FormsModule, HlmInputImports, HlmFieldImports, HlmCardImports, HlmLabelImports, HlmButtonImports, RouterLink, NgIcon],
  providers: [provideIcons({ lucideEye, lucideEyeOff })],
  selector: 'app-login',
  styleUrl: './login.css',
  templateUrl: './login.html',
})
export class Login {

  errorLogin: boolean = false;
  loginDados: LoginDados = {email : undefined, senha : undefined};
  
  private router = inject(Router);
  private authService = inject(AuthService);
  private tokenService = inject(TokenService);

  login(form: NgForm) {
    if (form.invalid) {
      form.control.markAllAsTouched();
      toast.warning('Preencha todos os campos corretamente.', {
        duration: 2000
      });
      return; // não chama a API
    }
    this.authService.login(this.loginDados).subscribe( {
      next: (res) => {
        this.tokenService.salvar(res.token);
        this.router.navigate(['/despesa/listar'])
        toast.success('Login realizado com sucesso!', {
          duration: 3000,
        });
      },
      error: (err) => {
        toast.error(err.error?.message ?? 'Falha ao realizar Login!', {
          duration: 3000
        })
      }
    })
  }

}
