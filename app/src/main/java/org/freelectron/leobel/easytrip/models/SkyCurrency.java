package org.freelectron.leobel.easytrip.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * Created by leobel on 1/5/17.
 */

public class SkyCurrency implements Serializable {
    public static SkyCurrency getDefault() {
        SkyCurrency currency = new SkyCurrency();
        currency.setCode("USD");
        currency.setSymbol("$");
        currency.setThousandsSeparator(",");
        currency.setDecimalSeparator(".");
        currency.setSymbolOnLeft(true);
        currency.setSpaceBetweenAmountAndSymbol(false);
        currency.setRoundingCoefficient(0);
        currency.setDecimalDigits(2);

        return currency;
    }

    @JsonProperty("Code")
    private String code;
    @JsonProperty("Symbol")
    private String symbol;
    @JsonProperty("ThousandsSeparator")
    private String thousandsSeparator;
    @JsonProperty("DecimalSeparator")
    private String decimalSeparator;
    @JsonProperty("SymbolOnLeft")
    private Boolean symbolOnLeft;
    @JsonProperty("SpaceBetweenAmountAndSymbol")
    private Boolean spaceBetweenAmountAndSymbol;
    @JsonProperty("RoundingCoefficient")
    private Integer roundingCoefficient;
    @JsonProperty("DecimalDigits")
    private Integer decimalDigits;

    @JsonProperty("Code")
    public String getCode() {
        return code;
    }

    @JsonProperty("Code")
    public void setCode(String code) {
        this.code = code;
    }

    @JsonProperty("Symbol")
    public String getSymbol() {
        return symbol;
    }

    @JsonProperty("Symbol")
    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    @JsonProperty("ThousandsSeparator")
    public String getThousandsSeparator() {
        return thousandsSeparator;
    }

    @JsonProperty("ThousandsSeparator")
    public void setThousandsSeparator(String thousandsSeparator) {
        this.thousandsSeparator = thousandsSeparator;
    }

    @JsonProperty("DecimalSeparator")
    public String getDecimalSeparator() {
        return decimalSeparator;
    }

    @JsonProperty("DecimalSeparator")
    public void setDecimalSeparator(String decimalSeparator) {
        this.decimalSeparator = decimalSeparator;
    }

    @JsonProperty("SymbolOnLeft")
    public Boolean getSymbolOnLeft() {
        return symbolOnLeft;
    }

    @JsonProperty("SymbolOnLeft")
    public void setSymbolOnLeft(Boolean symbolOnLeft) {
        this.symbolOnLeft = symbolOnLeft;
    }

    @JsonProperty("SpaceBetweenAmountAndSymbol")
    public Boolean getSpaceBetweenAmountAndSymbol() {
        return spaceBetweenAmountAndSymbol;
    }

    @JsonProperty("SpaceBetweenAmountAndSymbol")
    public void setSpaceBetweenAmountAndSymbol(Boolean spaceBetweenAmountAndSymbol) {
        this.spaceBetweenAmountAndSymbol = spaceBetweenAmountAndSymbol;
    }

    @JsonProperty("RoundingCoefficient")
    public Integer getRoundingCoefficient() {
        return roundingCoefficient;
    }

    @JsonProperty("RoundingCoefficient")
    public void setRoundingCoefficient(Integer roundingCoefficient) {
        this.roundingCoefficient = roundingCoefficient;
    }

    @JsonProperty("DecimalDigits")
    public Integer getDecimalDigits() {
        return decimalDigits;
    }

    @JsonProperty("DecimalDigits")
    public void setDecimalDigits(Integer decimalDigits) {
        this.decimalDigits = decimalDigits;
    }
}
