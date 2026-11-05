import { Component, inject } from '@angular/core';
import { RouterOutlet, RouterLinkWithHref, Router, RouterLink } from '@angular/router';
import { TokenService } from '../login/token.service';

import { NgIcon, provideIcons } from '@ng-icons/core';
import { lucideLogOut, lucidePlus } from '@ng-icons/lucide';

@Component({
  imports: [RouterOutlet, NgIcon, RouterLink],
  selector: 'navbar-component',
  styleUrl: './navbar.css',
  providers: [provideIcons({ lucideLogOut, lucidePlus })],
  templateUrl: './navbar.html',
})
export class Navbar {

  private router = inject(Router);
  private tokenService = inject(TokenService);

  sair() {
    this.tokenService.limpar();
    this.router.navigate(['/login']);
  }

}
