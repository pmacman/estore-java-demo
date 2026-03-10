package com.estore.controller.api.exception;

public class CustomerNotFoundException extends Exception {
    private int customerId;

    public static CustomerNotFoundException createWith(int customerId) {
        return new CustomerNotFoundException(customerId);
    }

    private CustomerNotFoundException(int customerId) {
        this.customerId = customerId;
    }

    @Override
    public String getMessage() {
        return "Customer '" + customerId + "' not found";
    }
}