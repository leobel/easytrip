package org.freelectron.leobel.easytrip.models;

/**
 * Created by leobel on 1/6/17.
 */

public class TooManyRequestException extends Exception {

    public TooManyRequestException(){
        this("Too Many Requests – There have been too many requests in the last minute.");
    }

    public TooManyRequestException(String message){
        super(message);
    }
}
