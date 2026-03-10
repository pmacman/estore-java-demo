package com.estore.controller.api.exception;

public class PaymentUnsuccessfulException extends Exception {
    private int customerId;

    public static PaymentUnsuccessfulException createWith(int customerId) {
        return new PaymentUnsuccessfulException(customerId);
    }

    private PaymentUnsuccessfulException(int customerId) {
        this.customerId = customerId;
    }

    @Override
    public String getMessage() {
        return "Payment unsuccessful for customer '" + customerId + "'";
    }
}