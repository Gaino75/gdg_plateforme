package com.gdg.service_paiement.exception;

public class PaiementNotFoundException extends RuntimeException{
    public PaiementNotFoundException(String message){
        super(message);
    }
}
