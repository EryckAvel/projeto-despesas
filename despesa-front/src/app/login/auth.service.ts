import { HttpClient } from '@angular/common/http';
import { inject, Service } from '@angular/core';
import { Observable } from 'rxjs';
import { LoginDados } from './loginDados';
import { CadastroLogin } from '../cadastro/cadastroLogin';
import { environment } from '../../environments/environment';
import { ResetSenhaRequest } from '../reset-senha/resetSenhaRequest';

interface LoginResponse { token: string; }

@Service()
export class AuthService {

    private http = inject(HttpClient);
    apiUrl = environment.apiUrl + '/auth';

    login(dados: LoginDados) : Observable<LoginResponse> {
        return this.http.post<LoginResponse>(this.apiUrl + '/login', dados)
    }

    cadastro(dados: CadastroLogin){
        return this.http.post<CadastroLogin>(this.apiUrl + '/cadastro', dados)
    }

    alterarSenha(dados: ResetSenhaRequest){
        return this.http.put(this.apiUrl + '/alterar-senha', dados)
    }

}
