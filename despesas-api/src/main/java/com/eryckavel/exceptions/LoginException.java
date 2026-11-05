package com.eryckavel.exceptions;

public class LoginException extends RuntimeException {

    public LoginException(String mensagem) {
        super(mensagem);
    }
}