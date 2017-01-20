package org.freelectron.leobel.easytrip.services;

import android.content.SharedPreferences;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.freelectron.leobel.easytrip.models.SkyCurrency;

import java.io.IOException;

/**
 * Created by leobel on 1/4/17.
 */

public class PreferenceServiceImpl implements PreferenceService {

    private static final String PREFERENCE_CURRENCY = "PREFERENCE_CURRENCY";
    private static final String PREFERENCE_COUNTRY = "PREFERENCE_COUNTRY";
    private static final String PREFERENCE_LOCALE = "PREFERENCE_COUNTRY";
    private static final String PREFERENCE_DATABASE_CREATED = "PREFERENCE_DATABASE_CREATED";


    private final SharedPreferences sharedPreferences;
    private final ObjectMapper objectMapper;

    public PreferenceServiceImpl(SharedPreferences sharedPreferences, ObjectMapper objectMapper) {
        this.sharedPreferences = sharedPreferences;
        this.objectMapper = objectMapper;
    }

    @Override
    public Boolean isDataBaseCreated() {
        return sharedPreferences.getBoolean(PREFERENCE_DATABASE_CREATED, false);
    }

    @Override
    public void setDataBaseCreated(Boolean isCreated) {
        SharedPreferences.Editor editor = getEditor();
        editor.putBoolean(PREFERENCE_DATABASE_CREATED, isCreated);
        editor.commit();
    }

    @Override
    public String getCountry() {
        return sharedPreferences.getString(PREFERENCE_COUNTRY, "US");
    }

    @Override
    public void setCountry(String country) {
        SharedPreferences.Editor editor = getEditor();
        editor.putString(PREFERENCE_COUNTRY, country);
        editor.commit();
    }

    private SharedPreferences.Editor getEditor() {
        return sharedPreferences.edit();
    }

    @Override
    public SkyCurrency getCurrency() {
        String currencyJson = sharedPreferences.getString(PREFERENCE_CURRENCY, "");
        if(!currencyJson.isEmpty()){
            try {
                return objectMapper.readValue(currencyJson, SkyCurrency.class);
            } catch (IOException e) {
                e.printStackTrace();
                return SkyCurrency.getDefault();
            }
        }
        else{
            return SkyCurrency.getDefault();
        }
    }

    @Override
    public void setCurrency(SkyCurrency skyCurrency) {
        try {
            String currencyJson = objectMapper.writeValueAsString(skyCurrency);
            SharedPreferences.Editor editor = getEditor();
            editor.putString(PREFERENCE_CURRENCY, currencyJson);
            editor.commit();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

    }

    @Override
    public String getLocale() {
        return sharedPreferences.getString(PREFERENCE_LOCALE, "en-US");
    }

    @Override
    public void setLocale(String locale) {
        SharedPreferences.Editor editor = getEditor();
        editor.putString(PREFERENCE_LOCALE, locale);
        editor.commit();
    }
}
