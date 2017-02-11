package org.freelectron.leobel.easytrip.services;

import org.freelectron.leobel.easytrip.models.PageResponse;
import org.freelectron.leobel.easytrip.models.Place;
import org.freelectron.leobel.easytrip.models.Response;

import java.util.List;

import rx.Observable;

/**
 * Created by leobel on 1/5/17.
 */

public interface LocalisationService {
    Observable<Response<List<Place>>> findPlacesByAutoSuggest(String phrase);
}
