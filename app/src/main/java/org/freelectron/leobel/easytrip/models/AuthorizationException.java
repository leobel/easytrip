package org.freelectron.leobel.easytrip.models;

/**
 * Created by leobel on 1/30/17.
 */

public class AuthorizationException extends Exception {

    public AuthorizationException(){
        this("Authentication or Authorization failed – You are not permitted to access that resource.");
    }

    public AuthorizationException(String message){
        super(message);
    }
}

