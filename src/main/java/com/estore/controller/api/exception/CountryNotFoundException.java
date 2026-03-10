package com.estore.controller.api.exception;

public class CountryNotFoundException extends Exception {
    private String countryIso2;

    public static CountryNotFoundException createWith(String countryIso2) {
        return new CountryNotFoundException(countryIso2);
    }

    private CountryNotFoundException(String countryIso2) {
        this.countryIso2 = countryIso2;
    }

    @Override
    public String getMessage() {
        return "Country '" + countryIso2 + "' not found";
    }
}