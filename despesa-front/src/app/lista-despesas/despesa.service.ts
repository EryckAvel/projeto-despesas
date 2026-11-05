import { HttpClient } from '@angular/common/http';
import { inject, Service } from '@angular/core';
import { Observable } from 'rxjs';
import { Despesa } from './despesa';
import { DespesaCadastro } from '../cadastrar-despesa/despesaCadastro';
import { environment } from '../../environments/environment';

@Service()
export class DespesaService {

    private http = inject(HttpClient);
    apiUrl = environment.apiUrl + '/despesa';

    listarDespesas(): Observable<Despesa[]> {
        return this.http.get<Despesa[]>(this.apiUrl);
    }

    buscarDespesa(iddespesa : number): Observable<Despesa> {
        return this.http.get<Despesa>(this.apiUrl + "/" + iddespesa);
    }

    cadastrarDespesas(despesa: DespesaCadastro){
        return this.http.post(this.apiUrl, despesa);
    }

    atualizarDespesa(despesa: DespesaCadastro, iddespesa : number){
        return this.http.put(this.apiUrl + "/" + iddespesa, despesa);
    }

    deletarDespesa(iddespesa : number){
        return this.http.delete(this.apiUrl + "/" + iddespesa)
    }
}
