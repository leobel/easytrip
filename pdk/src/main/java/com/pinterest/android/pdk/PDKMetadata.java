package com.pinterest.android.pdk;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * Created by leobel on 1/16/17.
 */

public class PDKMetadata implements Serializable {
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
