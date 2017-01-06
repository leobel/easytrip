package org.freelectron.leobel.easytrip.models;

/**
 * Created by leobel on 1/6/17.
 */

public class ForbiddenException extends Exception {

    public ForbiddenException(){
        this("Forbidden – The API Key was not supplied, or it was invalid, or it is not authorized to access the service.");
    }

    public ForbiddenException(String message){
        super(message);
    }
}
