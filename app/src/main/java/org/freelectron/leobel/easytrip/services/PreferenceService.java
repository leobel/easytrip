package org.freelectron.leobel.easytrip.services;

import org.freelectron.leobel.easytrip.models.SkyCurrency;

import java.util.Currency;

/**
 * Created by leobel on 1/4/17.
 */

public interface PreferenceService {

    String getCountry();
    void setCountry(String country);

    SkyCurrency getCurrency();
    void setCurrency(SkyCurrency skyCurrency);

    String getLocale();
    void setLocale(String locale);
}
