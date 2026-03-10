package com.estore.controller.api.exception;

public class CartNotFoundException extends Exception {
    private int customerId;

    public static CartNotFoundException createWith(int customerId) {
        return new CartNotFoundException(customerId);
    }

    private CartNotFoundException(int customerId) {
        this.customerId = customerId;
    }

    @Override
    public String getMessage() {
        return "Cart for customer '" + customerId + "' not found";
    }
}