import { computed, Service, signal } from "@angular/core";

@Service()
export class TokenService{

    private readonly CHAVE = 'token';

    private _token = signal<string | null>(localStorage.getItem(this.CHAVE));

    readonly token = this._token.asReadonly();
    readonly logado = computed(() => {
        const t = this._token();
        return t !== null && !this.expirado(t);
    });

    salvar(token: string) {
        localStorage.setItem(this.CHAVE, token);
        this._token.set(token);
    }

    limpar() {
        localStorage.removeItem(this.CHAVE);
        this._token.set(null);
    }

  // lê o campo "exp" do JWT (em segundos) para saber se já venceu
    private expirado(token: string): boolean {
        try {
            const payload = JSON.parse(atob(token.split('.')[1]));
            return payload.exp * 1000 < Date.now();
        } catch {
            return true; // token inválido conta como expirado
        }
    }

}