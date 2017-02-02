package org.freelectron.leobel.easytrip.models;

import com.pinterest.android.pdk.PDKException;

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

//    public static <T> Observable<? super Response<T>> handle(Observable<? extends Response<T>> observable, int source){
//        return Response.handleWillCard(((Observable<Response<T>>) observable), source);
//    }

    public static <T> Observable<Response<T>> handle(Observable<Response<T>> observable, int source){
        return observable
                .onErrorReturn(throwable -> {
                    if(throwable instanceof PDKException){
                        PDKException exception = (PDKException) throwable;
                        return Response.handleError(exception.getStatusCode(), exception.getDetailMessage(), source);
                    }
                    else return new Response<>(throwable, source);
                });
    }

    public static <T> Observable<PageResponse<T>> handlePageResponse(Observable<PageResponse<T>> observable, int source){
        return observable
                .onErrorReturn(throwable -> {
                    if(throwable instanceof PDKException){
                        PDKException exception = (PDKException) throwable;
                        Response<T> response = Response.handleError(exception.getStatusCode(), exception.getDetailMessage(), source);
                        return new PageResponse<>(response.getError(), response.getSource());
                    }
                    else return new PageResponse<>(throwable, source);
                });
    }



    public static <T> Response<T> handleError(int code, String message, int source) {
        return Response.handleError(code, message, null, source);
    }


    public static <T> Response<T> handleError(int code, String message, ResponseBody responseBody, int source) {
        switch (code){
            case 401:
                return new Response<>(new AuthorizationException(message), source);
            case 403:
                return new Response<>(new ForbiddenException(message), source);
            case 429:
                return new Response<>(new TooManyRequestException(message), source);
            case -1:
                return new Response<>(new InternetConnectionException(message), source);
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
