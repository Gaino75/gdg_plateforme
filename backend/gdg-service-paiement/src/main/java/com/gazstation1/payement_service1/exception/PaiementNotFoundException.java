package com.gazstation1.payement_service1.exception;

public class PaiementNotFoundException extends RuntimeException{
    public PaiementNotFoundException(String message){
        super(message);
    }
}
