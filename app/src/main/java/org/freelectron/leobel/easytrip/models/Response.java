package org.freelectron.leobel.easytrip.models;

import okhttp3.ResponseBody;
import rx.Observable;

/**
 * Created by leobel on 1/6/17.
 */

public class Response<T> {

    public static <T, K> Observable<Response<T>> handle(Observable<retrofit2.Response<K>> observable, ResponseConverter<K, T> converter, int source){
        return observable
                .map(response -> {
                    if(response.isSuccessful()){
                        return converter.convert(response.body());
                    }
                    else{
                        return Response.<T>handleError(response.code(), response.message(), response.errorBody(), source);
                    }

                })
                .onErrorReturn(throwable -> new Response<>(throwable, source));
    }

    public static <T> Response<T> handleError(int code, String message, ResponseBody responseBody, int source) {
        switch (code){
            case 403:
                return new Response<>(new ForbiddenException(), source);
            case 429:
                return new Response<>(new TooManyRequestException(), source);
            default:
                return new Response<>(new Exception("Server Error – An internal server error has occurred which has been logged."), source);
        }
    }

    private T value;

    private boolean successful;

    private Throwable error;

    private int source;

    public static final int CACHE = 1;
    public static final int NETWORK = 2;

    public Response(T value) {
        this(value,CACHE);
    }

    public Response(T value, int source) {
        this.value = value;
        this.successful = true;
        this.source = source;
    }

    public Response(Throwable error, int source) {
        this.error = error;
        this.successful = false;
        this.source = source;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public boolean isSuccessful() {
        return successful;
    }

    public void setSuccessful(boolean successful) {
        this.successful = successful;
    }

    public Throwable getError() {
        return error;
    }

    public void setError(Throwable error) {
        this.error = error;
    }

    public int getSource() {
        return source;
    }

    public void setSource(int source) {
        this.source = source;
    }

    public boolean isRemote(){
        return source == NETWORK;
    }

    public abstract static class ResponseConverter<K, T>{

        public abstract Response<T> convert(K source);
    }
}
