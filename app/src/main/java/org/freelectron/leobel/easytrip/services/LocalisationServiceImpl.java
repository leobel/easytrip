package org.freelectron.leobel.easytrip.services;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.freelectron.leobel.easytrip.models.Place;
import org.freelectron.leobel.easytrip.models.Response;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by leobel on 1/5/17.
 */

public class LocalisationServiceImpl implements LocalisationService {

    private final PreferenceService preferenceService;
    private final LocalisationServiceTemplate retrofitLocalisationService;

    public LocalisationServiceImpl(PreferenceService preferenceService, Retrofit retrofit){
        this.preferenceService = preferenceService;
        retrofitLocalisationService = retrofit.create(LocalisationServiceImpl.LocalisationServiceTemplate.class);
    }

    @Override
    public Observable<Response<List<Place>>> findPlacesByAutoSuggest(String query) {
        return Response.handle(
                retrofitLocalisationService.findPlacesByAutoSuggest(preferenceService.getCountry(), preferenceService.getCurrency().getCode(), preferenceService.getLocale(), query), Response.NETWORK)
                .map(response -> {
                    if(response.isSuccessful()){
                        return new Response<>(response.getValue().getPlaces(), response.getSource());
                    }
                    else{
                        return new Response<List<Place>>(response.getError(), response.getSource());
                    }
                });
    }

    public interface LocalisationServiceTemplate{
        @GET("autosuggest/v1.0/{country}/{currency}/{locale}/")
        Observable<retrofit2.Response<PlacesByAutoSuggestionResponse>> findPlacesByAutoSuggest(@Path("country") String country, @Path("currency") String currency, @Path("locale") String locale, @Query("query") String query);
    }

    static class PlacesByAutoSuggestionResponse {
        @JsonProperty("Places")
        private List<Place> places = null;

        @JsonProperty("Places")
        public List<Place> getPlaces() {
            return places;
        }

        @JsonProperty("Places")
        public void setPlaces(List<Place> places) {
            this.places = places;
        }
    }
}
