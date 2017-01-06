package org.freelectron.leobel.easytrip.services;

import android.support.v7.widget.RecyclerView;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.freelectron.leobel.easytrip.BuildConfig;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by leobel on 1/5/17.
 */

public class SecurityInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();
        Request request;
        if(original.method().toUpperCase().equals("GET")){
            //Build new request
            HttpUrl url = original.url().newBuilder()
                    .addQueryParameter("apiKey", BuildConfig.SKY_SCANNER_API_KEY)
                    .build();
            request = original.newBuilder().url(url).build();

        }
        else{
            RequestBody formBody = new FormBody.Builder()
                    .add("apiKey", BuildConfig.SKY_SCANNER_API_KEY)
                    .build();
            request = original.newBuilder()
                    .post(formBody)
                    .build();
        }
        return chain.proceed(request);
    }
}
