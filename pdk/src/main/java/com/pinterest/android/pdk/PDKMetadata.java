package com.pinterest.android.pdk;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by leobel on 1/16/17.
 */

public class PDKMetadata {
    @JsonProperty("place")
    private PDKPlace place;

    @JsonProperty("place")
    public PDKPlace getPlace() {
        return place;
    }

    @JsonProperty("place")
    public void setPlace(PDKPlace place) {
        this.place = place;
    }

}
