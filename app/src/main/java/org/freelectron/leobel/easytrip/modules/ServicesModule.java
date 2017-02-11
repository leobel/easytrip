package org.freelectron.leobel.easytrip.modules;

import android.app.Application;
import android.content.SharedPreferences;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.backends.okhttp3.OkHttpImagePipelineConfigFactory;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.pinterest.android.pdk.PDKClient;

import org.freelectron.leobel.easytrip.BuildConfig;
import org.freelectron.leobel.easytrip.services.LocalisationService;
import org.freelectron.leobel.easytrip.services.LocalisationServiceImpl;
import org.freelectron.leobel.easytrip.services.PinterestService;
import org.freelectron.leobel.easytrip.services.PinterestServiceImpl;
import org.freelectron.leobel.easytrip.services.PreferenceService;
import org.freelectron.leobel.easytrip.services.PreferenceServiceImpl;
import org.freelectron.leobel.easytrip.services.RealmService;
import org.freelectron.leobel.easytrip.services.RealmServiceImpl;
import org.freelectron.leobel.easytrip.services.SecurityInterceptor;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * Created by leobel on 1/4/17.
 */

@Module
public class ServicesModule {

    @Singleton @Provides
    public ObjectMapper providesObjectMapper(){
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.registerModule(new JodaModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        return objectMapper;
    }

    @Singleton @Provides
    public OkHttpClient providesHttpClient(Application context, PreferenceService preferenceService, ObjectMapper objectMapper){
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();

        // Configure Stetho only if debug
        if (BuildConfig.DEBUG) {
            httpClientBuilder.addNetworkInterceptor(new StethoInterceptor());

            HttpLoggingInterceptor networkInterceptor = new HttpLoggingInterceptor();
            networkInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            httpClientBuilder.addInterceptor(networkInterceptor);
        }

        // Security Interceptor
        SecurityInterceptor securityInterceptor = new SecurityInterceptor();
        httpClientBuilder.addInterceptor(securityInterceptor);

        OkHttpClient httpClient = httpClientBuilder.build();

        // Configure Fresco with okhttp
        ImagePipelineConfig config = OkHttpImagePipelineConfigFactory
                .newBuilder(context, httpClient)
                .build();

        Fresco.initialize(context, config);

        return httpClient;
    }

    @Singleton @Provides @Named("SKY_SCANNER_API")
    public Retrofit providesRetrofit(ObjectMapper objectMapper, OkHttpClient httpClient){
        return new Retrofit.Builder()
                .baseUrl(BuildConfig.SKY_SCANNER_BASE_API_URL)
                .client(httpClient)
                .addConverterFactory(JacksonConverterFactory.create(objectMapper))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }

    @Singleton @Provides
    public PDKClient providesPDKClient(){
        return PDKClient.getInstance();
    }

    @Singleton @Provides
    public PreferenceService providesPreferenceService(SharedPreferences sharedPreferences, ObjectMapper objectMapper){
        return new PreferenceServiceImpl(sharedPreferences, objectMapper);
    }

    @Singleton @Provides
    public LocalisationService providesLocalisationService(PreferenceService preferenceService, @Named("SKY_SCANNER_API") Retrofit retrofit){
        return new LocalisationServiceImpl(preferenceService, retrofit);
    }

    @Singleton @Provides
    public PinterestService providesPinterestService(PDKClient pdkClient){
        return new PinterestServiceImpl(pdkClient);
    }

    @Singleton @Provides
    public RealmService providesRealmService(){
        return new RealmServiceImpl();
    }
}
