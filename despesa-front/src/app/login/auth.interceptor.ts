import { HttpInterceptorFn } from "@angular/common/http";
import { TokenService } from "./token.service";
import { Router } from "@angular/router";
import { inject } from "@angular/core";
import { catchError, throwError } from "rxjs";

export const authInterceptor: HttpInterceptorFn = (req, next) => { 

    const tokenService = inject(TokenService);
    const router = inject(Router);
    const token = tokenService.token();

    const requisicao = token ? req.clone({ setHeaders: { Authorization: `Bearer ${token}` } }) : req;

    return next(requisicao).pipe(
        catchError(err => {
        if (err.status === 401) {       // token vencido ou inválido
            tokenService.limpar();
            router.navigate(['/login']);
        }
        return throwError(() => err);
    })
  );

}