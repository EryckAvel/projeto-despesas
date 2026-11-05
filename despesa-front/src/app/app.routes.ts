import { Routes } from '@angular/router';
import { Login } from './login/login';
import { ListaDespesas } from './lista-despesas/lista-despesas';
import { Navbar } from './navbar/navbar';
import { CadastrarDespesa } from './cadastrar-despesa/cadastrar-despesa';
import { Cadastro } from './cadastro/cadastro';
import { ResetSenha } from './reset-senha/reset-senha';

export const routes: Routes = [
    { 
        path: '',
        redirectTo: 'login',
        pathMatch: 'full' 
    },
    {
        path: 'login',
        component: Login
    },
    {
        path: 'cadastro',
        component: Cadastro,
        pathMatch: 'full' 
    },
    {
        path: 'alterar-senha',
        component: ResetSenha,
        pathMatch: 'full' 
    },
    {
        path: 'despesa',
        component: Navbar,
        children: [
            { path: '', redirectTo: 'lista', pathMatch: 'full' },
            { path: 'listar', component: ListaDespesas },
            { path: ':id/editar', component: CadastrarDespesa }
        ]
    }
];
