package com.pinterest.android.pdk;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * Created by leobel on 1/16/17.
 */

public class PDKImage implements Serializable{

    @JsonProperty("original")
    private PDKOriginal original;

    @JsonProperty("original")
    public PDKOriginal getOriginal() {
        return original;
    }

    @JsonProperty("original")
    public void setOriginal(PDKOriginal original) {
        this.original = original;
    }
}
