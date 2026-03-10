package com.estore.controller.api.exception;

public class PartnerNotFoundException extends Exception {
    private int partnerId;

    public static PartnerNotFoundException createWith(int partnerId) {
        return new PartnerNotFoundException(partnerId);
    }

    private PartnerNotFoundException(int partnerId) {
        this.partnerId = partnerId;
    }

    @Override
    public String getMessage() {
        return "Partner '" + partnerId + "' not found";
    }
}